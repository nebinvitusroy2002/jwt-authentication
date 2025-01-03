package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.model.Role;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> authenticatedUser() {
        User currentUser = userService.getAuthenticatedUser();
        Map<String, Object> response = createUserResponse(currentUser, "Fetched authenticated user successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> allUsers() {
        List<User> users = userService.allUsers();
        List<Map<String, Object>> userDataList = users.stream()
                .map(this::extractUserData)
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        response.put("Code", HttpStatus.OK.value());
        response.put("Status", true);
        response.put("message", "Fetched all users successfully");
        response.put("data", userDataList);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok("Role assigned successfully.");
    }


    private Map<String, Object> createUserResponse(User user, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        response.put("Code", HttpStatus.OK.value());
        response.put("Status", true);
        response.put("message", message);
        response.put("data", extractUserData(user));
        return response;
    }

    private Map<String, Object> extractUserData(User user) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", user.getUserId());
        data.put("name", user.getFullName());
        data.put("email", user.getEmail());
        data.put("role",user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        return data;
    }
}
