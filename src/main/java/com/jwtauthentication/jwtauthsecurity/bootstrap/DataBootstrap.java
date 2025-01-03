package com.jwtauthentication.jwtauthsecurity.bootstrap;

import com.jwtauthentication.jwtauthsecurity.model.Role;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.RoleRepository;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataBootstrap {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Bootstrapping roles and admin user...");

        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            return roleRepository.save(role);
        });

        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("USER");
            return roleRepository.save(role);
        });

        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("ADMIN USER");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));

            List<Role> roles = new ArrayList<>();
            roles.add(adminRole);
            roles.add(userRole);  // Add userRole if needed
            admin.setRoles(roles);

            userRepository.save(admin);
            log.info("Admin user created with email: admin@example.com");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
