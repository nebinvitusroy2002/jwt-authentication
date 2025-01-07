package com.jwtauthentication.jwtauthsecurity.service.authentication;

import com.jwtauthentication.jwtauthsecurity.dto.login.LoginUserDto;
import com.jwtauthentication.jwtauthsecurity.dto.register.RegisterUserDto;
import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.Role;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.RoleRepository;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;

    public User signUp(RegisterUserDto input) {
        log.info("Attempting to register user with email: {}", input.getEmail());

        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            log.error("User already registered with email: {}", input.getEmail());
            throw new BadRequestException(
                    messageSource.getMessage("error.badrequest", null, LocaleContextHolder.getLocale())
            );
        }

        User user = new User();
        user.setFullName(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        Role userRole = roleRepository.findByName("USER").orElseThrow(()->
                new AppException("Default USER role not found in the database"));
        user.setRoles(List.of(userRole));

        try {
            User savedUser = userRepository.save(user);
            log.info("User successfully registered with email: {}", savedUser.getEmail());
            return savedUser;
        } catch (Exception e) {
            log.error("Error occurred while registering user with email: {}", input.getEmail(), e);
            throw new AppException(
                    messageSource.getMessage("error.appexception", null, LocaleContextHolder.getLocale())
            );
        }
    }


    public User authenticate(LoginUserDto input) {
        log.info("Attempting to authenticate user with email: {}", input.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );

            User authenticatedUser = userRepository.findByEmail(input.getEmail())
                    .orElseThrow(() -> new BadRequestException(
                            messageSource.getMessage("error.badrequest", null, LocaleContextHolder.getLocale())
                    ));

            log.info("User successfully authenticated with email: {}", authenticatedUser.getEmail());
            return authenticatedUser;
        } catch (Exception e) {
            log.error("Authentication failed for user with email: {}", input.getEmail(), e);
            throw new BadRequestException(
                    messageSource.getMessage("error.badrequest", null, LocaleContextHolder.getLocale())
            );
        }
    }
}
