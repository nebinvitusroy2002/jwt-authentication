package com.jwtauthentication.jwtauthsecurity.service.authentication;

import com.jwtauthentication.jwtauthsecurity.dto.login.LoginUserDto;
import com.jwtauthentication.jwtauthsecurity.dto.register.RegisterUserDto;
import com.jwtauthentication.jwtauthsecurity.model.User;

import java.sql.SQLException;

public interface AuthenticationServiceInterface {
     User signUp(RegisterUserDto input, String roleName);
     User authenticate(LoginUserDto input);
}
