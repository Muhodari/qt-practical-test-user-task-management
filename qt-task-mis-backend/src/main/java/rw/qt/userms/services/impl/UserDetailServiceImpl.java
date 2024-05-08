package rw.qt.userms.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rw.qt.userms.models.UserAccount;

import rw.qt.userms.repositories.IUserRepository;
import rw.qt.userms.security.dtos.UserDetailsImpl;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<UserAccount> user = userRepository.findByEmailAddress(username);

        if(user.isPresent())   {
            UserAccount userAccount =  user.get();
            log.info("Logged in username {} and Id {}", userAccount.getEmailAddress(), userAccount.getId());
            return  UserDetailsImpl.build(userAccount);

        } else{
            throw new UsernameNotFoundException("User not found");
        }
    }

}
