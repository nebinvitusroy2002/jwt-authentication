package com.jwtauthentication.jwtauthsecurity.service;

import com.jwtauthentication.jwtauthsecurity.dto.LoginUserDto;
import com.jwtauthentication.jwtauthsecurity.dto.RegisterUserDto;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(RegisterUserDto input){
        if(input.getEmail() == null || input.getEmail().isEmpty()){
            throw new BadRequestException("Email is required");
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(input.getEmail()).matches()){
            throw new BadRequestException("Invalid email format");
        }
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty or null");
        }

        if (input.getPassword() == null || input.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty or null");
        }
        User user = new User().setFullName(input.getName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        if (input.getEmail() == null || input.getEmail().isEmpty()) {
            throw new BadRequestException("Email cannot be empty.");
        }
        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new BadRequestException("Password cannot be empty.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword()));
        return userRepository.findByEmail(input.getEmail()).orElseThrow(()-> new BadRequestException("Unable to find the user..."));
    }
}
