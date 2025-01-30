package com.jwtauthentication.jwtauthsecurity.service.role;

import com.jwtauthentication.jwtauthsecurity.model.Role;

import java.util.List;

public interface RoleServiceInterface {
    Role createRole(String roleName);
    List<Role> getAllRoles();
    Role getRoleById(Long roleId);
    void deleteRole(Long roleId);
}
