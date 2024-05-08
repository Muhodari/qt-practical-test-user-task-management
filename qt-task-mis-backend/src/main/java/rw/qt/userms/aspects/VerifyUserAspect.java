package rw.qt.userms.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import rw.qt.userms.annotations.VerifyUser;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.repositories.IUserRepository;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * This class is used to verify if the user is the owner of the resource
 * being accessed.
 */

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyUserAspect {

    private final IUserRepository userRepository;

    @Around("@annotation(verifyUser) && execution(* *(.., @org.springframework.web.bind.annotation.PathVariable (*), ..))")
    public Object verifyUser(ProceedingJoinPoint joinPoint, VerifyUser verifyUser) throws Throwable {
        UUID userId = getUserId(joinPoint, verifyUser);

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        UserAccount findByEmail = userRepository.findByEmailAddress(username).orElseThrow(() -> new ResourceNotFoundException("UserAccount", "email", username));

        if(!findByEmail.getId().equals(userId)){
            throw new AccessDeniedException("Unauthorized");  // User is not authorized
        }else{
            return joinPoint.proceed();  // User is authorized, proceed with the method execution
        }
    }

    private static UUID getUserId(ProceedingJoinPoint joinPoint, VerifyUser verifyUserId) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String argName = verifyUserId.value();  // The name of the argument to be verified

        // Get the argument values and names
        Object[] args = joinPoint.getArgs();
        String[] argNames = signature.getParameterNames();

        // Find the argument to be verified
        UUID userId = null;
        for (int i = 0; i < argNames.length; i++) {
            if (argNames[i].equals(argName) && args[i] instanceof UUID) {
                userId = (UUID) args[i];
                break;
            }
        }

        if (userId == null) {
            throw new IllegalArgumentException("User ID argument not found or not of type UUID");
        }
        return userId;
    }
}
