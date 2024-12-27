package com.jwtauthentication.jwtauthsecurity.repository;

import com.jwtauthentication.jwtauthsecurity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
}
