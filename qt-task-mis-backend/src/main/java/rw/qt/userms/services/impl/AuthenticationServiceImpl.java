package rw.qt.userms.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rw.qt.userms.models.dtos.ForgotPasswordDTO;
import rw.qt.userms.services.*;
import rw.qt.userms.utils.Constants;
import rw.qt.userms.utils.OTPUtil;
import rw.qt.userms.exceptions.BadRequestAlertException;
import rw.qt.userms.exceptions.ResourceNotFoundException;

import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.dtos.VerifyOtpDTO;
import rw.qt.userms.models.enums.EOTPStatus;
import rw.qt.userms.models.enums.EUserStatus;
import rw.qt.userms.repositories.IUserRepository;
import rw.qt.userms.security.dtos.JwtAuthenticationResponse;
import rw.qt.userms.security.dtos.LoginRequest;
import rw.qt.userms.security.dtos.LoginResponseDTO;
import rw.qt.userms.security.dtos.UserDetailsImpl;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;

    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;


    private final IUserService userService;

    @Override
    public LoginResponseDTO signin(LoginRequest request) throws ResourceNotFoundException {

            UserAccount user = null;

            log.info("logging in with email:'{}'" , request.getLogin());
         try{

            user = userRepository.findByEmailAddress(request.getLogin()).orElseThrow();

             log.info("user found "+ user.getEmailAddress());

             // fail when (1) have login have failed multiple times, (2) when user is not found
             if((user.getLoginFailureCount() != null &&  user.getLoginFailureCount() >= 5) ||
                     user == null ||
                     user.getStatus().equals(EUserStatus.ADMIN_LOCKED))
                 throw new IllegalArgumentException();


             // auth user
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));



             //if valid credentials, save last login
             user.setLastLogin(LocalDateTime.now(ZoneId.of("Africa/Kigali")));
             user.setLoginFailureCount(0L);
             user.setAccountLocked(false);
             userRepository.save(user);


             List<GrantedAuthority> privileges = new ArrayList<>();

             user.setAuthorities(privileges);

             log.info("testing privileges 1");

             var jwt = JwtAuthenticationResponse.builder()
                     .accessToken(jwtService.generateToken(UserDetailsImpl.build(user))).tokenType(Constants.TOKEN_TYPE).build();
             log.info("testing privileges 2");


//             RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
//             jwt.setRefreshToken(refreshToken.getToken());

             return LoginResponseDTO.builder().token(jwt).build();

         }catch(Exception e){
            log.info("Login failed :" + request.getLogin());
            log.info("Exception: " + e.toString());
             if(user != null){
                 if(user.getStatus().equals(EUserStatus.ADMIN_LOCKED)){
                     throw new IllegalArgumentException("Your account is locked, contact admin!");
                 } else {
                     this.userService.processAccountLockingEligibility(user);
                 }
             }


             e.printStackTrace();
             throw new IllegalArgumentException("Invalid email or password.");
         }


    }

    @Override
    public UserAccount getAuthenticatedUser(String token) throws ResourceNotFoundException {
        String  username=  this.jwtService.extractUserName(token);
        Optional<UserAccount> userAccount = this.userRepository.findByEmailAddress(username);
        if (userAccount.isPresent()) {
            return userAccount.get();
        }
        else throw new ResourceNotFoundException("User", "username", username);
    }
    @Override
    public String initiateForgotPassword(ForgotPasswordDTO forgotPassword) throws  ResourceNotFoundException {
        Optional<UserAccount> userAccount = this.userRepository.findByEmailAddress(forgotPassword.getEmailAddress());
        log.info("Initiating forgot password : " + forgotPassword.toString());
        if(userAccount.isPresent()){
            UserAccount user = userAccount.get();
            LocalDateTime now = LocalDateTime.now();

            String otp = OTPUtil.generateOtp();

            LocalDateTime oneHourLater = now.plusMinutes(5);
            user.setOtp(otp);
            user.setOtpStatus(EOTPStatus.NOT_USED);
            user.setOtpExpiryDate(oneHourLater);

            log.info("Forgot password initiated with OTP: " + otp);


            this.userRepository.save(user);
            return otp;

        } else throw new ResourceNotFoundException("User", "email", forgotPassword.getEmailAddress());
    }

    @Override
    public boolean verifyOTP(VerifyOtpDTO verifyOtp) throws ResourceNotFoundException {
        Optional<UserAccount> userAccount = this.userRepository.findByEmailAddress(verifyOtp.getEmailAddress());
        log.info("Verifying OTP: " + verifyOtp.toString());
        if (!userAccount.isPresent()) {
            throw new ResourceNotFoundException("User", "email", verifyOtp.getEmailAddress());
        }

        UserAccount user = userAccount.get();
        LocalDateTime now = LocalDateTime.now();

        boolean isOtpExpired = user.getOtpExpiryDate() != null && (now.isAfter(user.getOtpExpiryDate()));

        if (isOtpExpired) {
            user.setOtpStatus(EOTPStatus.EXPIRED);
            this.userRepository.save(user);
            throw new BadRequestAlertException("exceptions.badRequest.expiredOtp");
        }

        boolean isOtpNotUsed = user.getOtpStatus() != null && user.getOtpStatus() == EOTPStatus.NOT_USED;
        boolean isOtpMatching = user.getOtp() != null ? user.getOtp().equals(verifyOtp.getOtp()) : false;
        if (!isOtpMatching && isOtpNotUsed) {
            throw new BadRequestAlertException("exceptions.badRequest.invalidOtp");
        }

        user.setOtpStatus(EOTPStatus.VERIFIED);
        this.userRepository.save(user);

        log.info("OTP Verified: " + user.getEmailAddress().toString());

        return true;
    }

    @Override
    public UserAccount verifyUserOwnership(UUID userId) throws ResourceNotFoundException {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        UserAccount findByEmail = userRepository.findByEmailAddress(username).orElseThrow(() -> new ResourceNotFoundException("UserAccount", "email", username));

        if( findByEmail.getId() != userId) throw new BadRequestAlertException("You are not allowed to perform this action");

        return findByEmail;
    }
}
