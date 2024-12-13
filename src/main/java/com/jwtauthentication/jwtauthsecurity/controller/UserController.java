package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.dto.UserResponseDto;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> authenticatedUser(){
        User currentUser = userService.getAuthenticatedUser();
        UserResponseDto userResponseDto = createUserResponse(currentUser);
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> allUsers(){
        List<User> users = userService.allUsers();
        List<UserResponseDto> userResponseDtos =users.stream()
                .map(this::createUserResponse)
                .toList();
        return ResponseEntity.ok(userResponseDtos);
    }

    private UserResponseDto createUserResponse(User user){
        return UserResponseDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .name(user.getFullName())
                .statusCode(HttpStatus.OK.value())
                .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .build();
    }
}
