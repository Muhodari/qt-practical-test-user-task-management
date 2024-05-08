package rw.qt.userms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rw.qt.userms.models.dtos.*;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.enums.EUserStatus;

import java.util.List;
import java.util.UUID;


public interface IUserService {
    Page<UserAccount> getAllPaginated(Pageable pageable) throws ResourceNotFoundException;

    UserAccount getPureUserById(UUID id) throws ResourceNotFoundException;
    UserAccount create(CreateUserDTO dto) throws DuplicateRecordException, ResourceNotFoundException;
    UserAccount getById(UUID id) throws ResourceNotFoundException;
    UserAccount updateById(UUID id, UpdateUserDTO dto) throws ResourceNotFoundException, DuplicateRecordException;
    UserAccount updatePassword(UUID id, UpdatePasswordDTO dto) throws ResourceNotFoundException;
    UserAccount unlockAccountById(UUID id) throws ResourceNotFoundException;


    void processAccountLockingEligibility(UserAccount user)  throws ResourceNotFoundException;

    UserAccount incrementLoginFailureCount(UserAccount user);

    void deleteById(UUID id) throws ResourceNotFoundException;
    UserAccount getLoggedInUser() throws ResourceNotFoundException;

}