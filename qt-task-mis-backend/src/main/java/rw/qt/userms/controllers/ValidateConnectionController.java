package rw.qt.userms.controllers;


import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.qt.userms.security.dtos.ConnValidationResponse;

import java.util.List;

@Slf4j
@Hidden
@RestController
@RequestMapping("/api/v1/validateToken")
@RequiredArgsConstructor
public class ValidateConnectionController {


    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> validatePost() {
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.POST.name())
                .isAuthenticated(true).build());
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnValidationResponse> validateGet(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//UserDetails userDetails = request.getUserPrincipal().getName();
        log.info("User has username: " + auth.getName());
        log.info("User has authorities: " + auth.getAuthorities());
        log.info("Request is authenticated: ", auth.isAuthenticated());

        log.info("Request token: ", request.getHeader("Authorization"));
        String username = auth.getName();
        String token = (String) request.getAttribute("jwt");
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) auth.getAuthorities();
        return ResponseEntity.ok(ConnValidationResponse.builder().status("OK").methodType(HttpMethod.GET.name())
                        .username(username).token(token).authorities(grantedAuthorities)
                .isAuthenticated(true).build());
    }


}
