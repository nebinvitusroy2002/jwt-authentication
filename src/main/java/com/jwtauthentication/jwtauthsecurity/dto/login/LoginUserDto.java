package com.jwtauthentication.jwtauthsecurity.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotBlank(message = "Password cannot be null")
    private String password;
}
