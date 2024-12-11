package com.jwtauthentication.jwtauthsecurity.error;

public class UserServiceException extends RuntimeException{
    public UserServiceException(String message) {
        super(message);
    }
}
