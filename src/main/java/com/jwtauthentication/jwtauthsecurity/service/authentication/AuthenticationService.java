package com.jwtauthentication.jwtauthsecurity.service.authentication;

import com.jwtauthentication.jwtauthsecurity.dto.login.LoginUserDto;
import com.jwtauthentication.jwtauthsecurity.dto.register.RegisterUserDto;
import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signUp(RegisterUserDto input) throws SQLException {
        log.info("Attempting to register user with email: {}", input.getEmail());

        List<String> validRoles = List.of("USER", "ADMIN");
        String role = input.getRole();

        if (role != null && !validRoles.contains(role)) {
            log.error("Invalid role provided: {}", role);
            throw new BadRequestException("Invalid role provided");
        }

        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            log.error("User already registered with email: {}", input.getEmail());
            throw new BadRequestException("User already registered with this email");
        }

        if (role == null || role.isEmpty()) {
            role = "USER";
        }

        User user = new User().setFullName(input.getName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(Set.of(role));

        try {
            User savedUser = userRepository.save(user);
            log.info("User successfully registered with email: {}", savedUser.getEmail());
            return savedUser;
        }catch (Exception e){
            log.error("Error occurred while registering user with email: {}", input.getEmail(), e);
            throw new AppException("User not registered");
        }
    }

    public User authenticate(LoginUserDto input){
        log.info("Attempting to authenticate user with email: {}", input.getEmail());
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword()));
            User authenticatedUser = userRepository.findByEmail(input.getEmail())
                    .orElseThrow(()-> new BadRequestException("Unable to find the user..."));
            log.info("User successfully authenticated with email: {}", authenticatedUser.getEmail());
            return authenticatedUser;
        }catch (Exception e){
            log.error("Authentication failed for user with email: {}", input.getEmail(), e);
            throw new BadRequestException("Unable to authenticate the user");
        }
    }
}
