package com.jwtauthentication.jwtauthsecurity.response;

import lombok.Getter;

@Getter
public class LoginResponse {

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    private String token;
    private long expiresIn;
}
