package rw.qt.userms.services;


import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.dtos.ForgotPasswordDTO;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.dtos.VerifyOtpDTO;
import rw.qt.userms.security.dtos.LoginRequest;
import rw.qt.userms.security.dtos.LoginResponseDTO;

import java.util.UUID;

public interface IAuthenticationService {


    LoginResponseDTO signin(LoginRequest request) throws ResourceNotFoundException;


    UserAccount getAuthenticatedUser(String token) throws ResourceNotFoundException;

    String initiateForgotPassword(ForgotPasswordDTO forgotPassword) throws  ResourceNotFoundException;

    boolean verifyOTP(VerifyOtpDTO verifyOtp) throws ResourceNotFoundException;

    UserAccount verifyUserOwnership(UUID userId) throws ResourceNotFoundException;
}
