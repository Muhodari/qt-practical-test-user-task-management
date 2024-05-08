package rw.qt.userms.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.qt.userms.models.dtos.CreateUserDTO;
import rw.qt.userms.models.dtos.ForgotPasswordDTO;
import rw.qt.userms.services.IUserService;
import rw.qt.userms.models.dtos.VerifyOtpDTO;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.domains.ApiResponse;
import rw.qt.userms.security.dtos.LoginRequest;
import rw.qt.userms.security.dtos.LoginResponseDTO;
import rw.qt.userms.services.IAuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {
    private final IAuthenticationService authenticationService;
    private final IUserService userService;



    @PostMapping("/signup")
    public ResponseEntity<UserAccount> signup(@Valid  @RequestBody CreateUserDTO request) throws DuplicateRecordException, ResourceNotFoundException {
        return ResponseEntity.ok(userService.create(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDTO> signin(
            @RequestBody LoginRequest request) throws ResourceNotFoundException {
        return ResponseEntity.ok(authenticationService.signin(request));
    }



    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordDTO request) throws ResourceNotFoundException {
        return ResponseEntity.ok(new ApiResponse<>(authenticationService.initiateForgotPassword(request), (Object) "", HttpStatus.OK));
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestBody VerifyOtpDTO request) throws ResourceNotFoundException {
        return ResponseEntity.ok(new ApiResponse<>(authenticationService.verifyOTP(request), "", HttpStatus.OK));
    }


    @GetMapping("/currentUser")
    public ResponseEntity<UserAccount> authUser() throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getLoggedInUser());
    }

    @Override
    protected String getEntityName() {
        return "Auth";
    }
}
