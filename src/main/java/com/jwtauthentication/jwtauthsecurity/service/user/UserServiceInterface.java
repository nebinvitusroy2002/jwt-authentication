package com.jwtauthentication.jwtauthsecurity.service.user;

import com.jwtauthentication.jwtauthsecurity.dto.user.UserResponseDto;
import com.jwtauthentication.jwtauthsecurity.model.User;

import java.util.List;

public interface UserServiceInterface {
     List<UserResponseDto> allUsers();
     UserResponseDto getAuthenticatedUserResponse();
     User getAuthenticatedUser();
     User loadUserByUsername(String userEmail);
}
