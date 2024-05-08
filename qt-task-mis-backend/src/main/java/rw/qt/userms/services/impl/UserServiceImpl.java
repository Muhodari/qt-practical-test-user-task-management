package rw.qt.userms.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.qt.userms.models.dtos.*;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.services.*;
import rw.qt.userms.exceptions.BadRequestAlertException;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.enums.EUserStatus;
import rw.qt.userms.repositories.IUserRepository;
import rw.qt.userms.security.dtos.CustomUserDTO;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private final IJwtService jwtService;



    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Page<UserAccount> getAllPaginated(Pageable pageable) throws ResourceNotFoundException {
        log.info("Fetching all user paginated");
        Page<UserAccount> users = this.userRepository.findAllByStatusNot(EUserStatus.DELETED, pageable);
//        return this.attachCenters(users);
        return users;
    }



    @Override
    public UserAccount create(CreateUserDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
        log.info("Creating user with details " + dto.toString());


        log.info("Finding duplicate user by email address '" + dto.getEmailAddress());
        Optional<UserAccount> duplicateEmailAddress = this.userRepository.findByEmailAddress(dto.getEmailAddress());
        if (duplicateEmailAddress.isPresent())
            throw new DuplicateRecordException("User", "emailAddress", dto.getEmailAddress());

        log.info("Finding duplicate user by phone number '" + dto.getPhoneNumber());
        Optional<UserAccount> duplicatePhoneNumber = this.userRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (duplicatePhoneNumber.isPresent())
            throw new DuplicateRecordException("User", "phoneNumber", dto.getPhoneNumber());



        UserAccount userAccount = new UserAccount(dto);

        userAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
        userAccount.setCredentialsExpired(false);
        userAccount.setCredentialsExpiryDate(LocalDateTime.now().plusMonths(12).toString());
        userAccount.setAccountEnabled(true);
        userAccount.setAccountExpired(false);
        userAccount.setAccountLocked(false);
        userAccount.setDeletedFlag(false);
        this.userRepository.save(userAccount);

        return userAccount;
    }

    @Override
    public UserAccount getById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching User by id '" + id.toString() + "'");

        return this.getPureUserById(id);
    }

    @Override
    public UserAccount getPureUserById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching pure User by id '" + id.toString() + "'");

        UserAccount userAccount = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        return userAccount;
    }


    @Override
    public UserAccount updateById(UUID id, UpdateUserDTO dto) throws ResourceNotFoundException, DuplicateRecordException {
        log.info("Updating User by id '" + id.toString() + "'");
        UserAccount userAccount = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );


        log.info("Finding duplicate User by emailAddress '" + dto.getEmailAddress());
        Optional<UserAccount> duplicateEmailAddress = this.userRepository.findByEmailAddress(dto.getEmailAddress());
        if (duplicateEmailAddress.isPresent() && !duplicateEmailAddress.get().getId().equals(id))
            throw new DuplicateRecordException("User", "emailAddress", dto.getEmailAddress());

        log.info("Finding duplicate User by phoneNumber '" + dto.getPhoneNumber());
        Optional<UserAccount> duplicatePhoneNumber = this.userRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (duplicatePhoneNumber.isPresent() && !duplicatePhoneNumber.get().getId().equals(id))
            throw new DuplicateRecordException("User", "phoneNumber", dto.getEmailAddress());

        userAccount.setEmailAddress(dto.getEmailAddress());
        userAccount.setPhoneNumber(dto.getPhoneNumber());
        userAccount.setFirstName(dto.getFirstName());
        userAccount.setLastName(dto.getLastName());
        userAccount.setGender(dto.getGender());
        userAccount.setStatus(dto.getStatus());
        this.userRepository.save(userAccount);

       return userAccount;
    }

    @Override
    public UserAccount updatePassword(UUID id, UpdatePasswordDTO dto) throws ResourceNotFoundException {
        log.info("Updating User by id '" + id.toString() + "'");
        UserAccount userAccount = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );

        if(!passwordEncoder.matches(dto.getOldPassword(), userAccount.getPassword())) {
            throw new BadRequestAlertException("exceptions.badRequest.passwordMissMatch");
        }
        userAccount.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.userRepository.save(userAccount);

        return userAccount;
    }


    @Override
    public UserAccount unlockAccountById(UUID id) throws ResourceNotFoundException {
        UserAccount userAccount = this.getPureUserById(id);
        userAccount.setStatus(EUserStatus.PENDING);
        userAccount.setAccountLocked(false);
        userAccount.setLoginFailureCount(0L);
        this.userRepository.save(userAccount);

        CustomUserDTO userDTO = this.jwtService.extractLoggedInUser();

        return userAccount;
    }

    @Override
    public void processAccountLockingEligibility(UserAccount user)  throws ResourceNotFoundException {
        // on first login failure, save it
        if(user.getLoginFailureCount() == null){
            this.incrementLoginFailureCount(user);
        }
        // 3rd or 4th login failure we give warning
        else if( user.getLoginFailureCount() > 2 && user.getLoginFailureCount() < 5){
            this.incrementLoginFailureCount(user);
            throw new IllegalArgumentException("Multiple login failures; account may lock.");
        }
        // > 5 warnings we lock account
        else if(user.getLoginFailureCount() >= 5){
            log.info("Logging in for Locked account :" + user.getEmailAddress());

            if(!user.getStatus().equals(EUserStatus.LOGIN_LOCKED)){
                // TODO: send email when user account is locked
                user.setStatus(EUserStatus.LOGIN_LOCKED);
                user.setAccountLocked(true);
                userRepository.save(user);
            }

            throw new IllegalArgumentException("Account is locked");

        }else {
            this.incrementLoginFailureCount(user);
        }
    }

    @Override
    public UserAccount incrementLoginFailureCount(UserAccount user){
        if(user.getLoginFailureCount() == null)
            user.setLoginFailureCount(1L);
        else
            user.setLoginFailureCount(user.getLoginFailureCount()+1);

        return userRepository.save(user);
    }


    @Override
    public void deleteById(UUID id) throws ResourceNotFoundException {
        log.info("Deleting Rank by id '" + id.toString() + "'");
        UserAccount userAccount = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())
        );
        userAccount.setStatus(EUserStatus.DELETED);
        userAccount.setDeletedFlag(true);
        this.userRepository.save(userAccount);

        CustomUserDTO userDTO = this.jwtService.extractLoggedInUser();
    }
    @Override
    public UserAccount getLoggedInUser() throws ResourceNotFoundException {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Here is goes ...............");
        System.out.println(principal);

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<UserAccount> findByEmail = userRepository.findByEmailAddress(username);
        if (findByEmail.isPresent()) {
            return this.getById(findByEmail.get().getId());
        }
        else {
            return null;
        }
    }




}
