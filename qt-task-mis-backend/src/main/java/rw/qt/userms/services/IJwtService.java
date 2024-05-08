package rw.qt.userms.services;

import org.springframework.security.core.userdetails.UserDetails;
import rw.qt.userms.security.dtos.CustomUserDTO;

public interface IJwtService {

    String extractUserName(String token);
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);

    CustomUserDTO extractLoggedInUser();
}
