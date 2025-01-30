package com.jwtauthentication.jwtauthsecurity.service.user;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.Role;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.RoleRepository;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import com.jwtauthentication.jwtauthsecurity.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final RoleService roleService;

    public List<User> allUsers() {
        log.info("Fetching all users from the database");
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching users from the database: {}", e.getMessage());
            throw new AppException("Error fetching users from the database.");
        }
    }

    public User getAuthenticatedUser() {
        log.info("Retrieving authenticated user from security context");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                log.warn("No authenticated user found");
                throw new AppException(messageSource.getMessage("error.unexpected", null, LocaleContextHolder.getLocale()));
            }
            String userEmail = authentication.getName();
            log.info("Authenticated user's email: {}", userEmail);
            return findByUsername(userEmail);
        } catch (Exception e) {
            log.error("Error retrieving authenticated user: {}", e.getMessage());
            throw new AppException("Error retrieving authenticated user.");
        }
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        try {
            User user = getUserById(userId);
            Role role = roleService.getRoleById(roleId);

            user.getRoles().clear();
            user.getRoles().add(role);
            userRepository.save(user);
            log.info("Assigned role {} to user {}",role.getName(),user.getEmail());
        } catch (BadRequestException e) {
            log.error("User not found: {}", e.getMessage());
            throw e;
        } catch (AppException e) {
            log.error("Error assigning role to user: {}", e.getMessage());
            throw new AppException("Error assigning role to user.");
        }
    }


    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
