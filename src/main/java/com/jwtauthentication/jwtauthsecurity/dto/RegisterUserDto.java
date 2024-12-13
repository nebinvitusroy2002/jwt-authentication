package com.jwtauthentication.jwtauthsecurity.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4,message = "Password must be at least 4 characters long")
    private String password;
    @NotBlank(message = "Name cannot be null")
    private String name;
}
