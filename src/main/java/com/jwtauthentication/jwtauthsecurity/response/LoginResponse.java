package com.jwtauthentication.jwtauthsecurity.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


public class LoginResponse {
    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    private String token;
    private long expiresIn;
}
