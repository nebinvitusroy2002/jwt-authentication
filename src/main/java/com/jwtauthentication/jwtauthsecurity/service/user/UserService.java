package com.jwtauthentication.jwtauthsecurity.service.user;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public List<User> allUsers(){
        log.info("Fetching all users from the database");
      return new ArrayList<>(userRepository.findAll());
    }

    public User getAuthenticatedUser(){
        log.info("Retrieving authenticated user from security context");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            log.warn("No authenticated user found");
            throw new AppException("No authenticated user found");
        }
        String userEmail = authentication.getName();
        log.info("Authenticated user's email: {}",userEmail);
        return userRepository.findByEmail(userEmail).orElseThrow(()->{
            log.error("Authenticated user not found in the database for email: {}",userEmail);
            return new AppException("Authenticated user not found in database");
        });
    }

    @Override
    public User loadUserByUsername(String userEmail) {
        log.info("Loading user by email: {}",userEmail);
        return userRepository.findByEmail(userEmail)
                .orElseThrow(()->{
                    log.error("User not found with email: {}",userEmail);
                    return new AppException("User not found with email: "+userEmail);
                });
    }

}
