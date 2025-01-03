package com.jwtauthentication.jwtauthsecurity.service.role;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.model.Role;
import com.jwtauthentication.jwtauthsecurity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleServiceInterface{
    private final RoleRepository roleRepository;

    public Role createRole(String roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new AppException("Role already exists.");
        }

        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException("Role not found."));
    }

    public Role updateRole(Long roleId, String newRoleName) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException("Role not found."));

        if (roleRepository.findByName(newRoleName).isPresent()) {
            throw new AppException("Role with the new name already exists.");
        }

        role.setName(newRoleName);
        return roleRepository.save(role);
    }

    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new AppException("Role not found.");
        }
        roleRepository.deleteById(roleId);
    }
}

