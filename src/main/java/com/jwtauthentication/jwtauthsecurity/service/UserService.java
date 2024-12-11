package com.jwtauthentication.jwtauthsecurity.service;

import com.jwtauthentication.jwtauthsecurity.error.UserServiceException;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            throw new UserServiceException("No authenticated user found");
        }
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail).orElseThrow(()->new UserServiceException("Authenticated user not found in database"));
    }
}
