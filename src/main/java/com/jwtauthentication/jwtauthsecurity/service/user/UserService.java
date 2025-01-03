package com.jwtauthentication.jwtauthsecurity.service.user;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.model.Role;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.RoleRepository;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;

    public List<User> allUsers() {
        log.info("Fetching all users from the database");
        return new ArrayList<>(userRepository.findAll());
    }

    public User getAuthenticatedUser() {
        log.info("Retrieving authenticated user from security context");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            log.warn("No authenticated user found");
            throw new AppException(
                    messageSource.getMessage("error.unexpected", null, LocaleContextHolder.getLocale())
            );
        }
        String userEmail = authentication.getName();
        log.info("Authenticated user's email: {}", userEmail);
        return findByUsername(userEmail);
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }


    public User findByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("User with username {} not found", username);
                    return new AppException(
                            messageSource.getMessage("error.badrequest", null, LocaleContextHolder.getLocale())
                    );
                });
    }

    @Override
    public User loadUserByUsername(String userEmail) {
        log.info("Loading user by username (email): {}", userEmail);
        return findByUsername(userEmail);
    }
}
