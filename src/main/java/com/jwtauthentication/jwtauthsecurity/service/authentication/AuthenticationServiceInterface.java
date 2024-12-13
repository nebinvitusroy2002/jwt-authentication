package com.jwtauthentication.jwtauthsecurity.service.authentication;

import com.jwtauthentication.jwtauthsecurity.dto.LoginUserDto;
import com.jwtauthentication.jwtauthsecurity.dto.RegisterUserDto;
import com.jwtauthentication.jwtauthsecurity.model.User;

import java.sql.SQLException;

public interface AuthenticationServiceInterface {
     User signUp(RegisterUserDto input) throws SQLException;
     User authenticate(LoginUserDto input);
}
