package com.jwtauthentication.jwtauthsecurity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
public class LoginUserDto {
    private String email;
    private String password;
}
