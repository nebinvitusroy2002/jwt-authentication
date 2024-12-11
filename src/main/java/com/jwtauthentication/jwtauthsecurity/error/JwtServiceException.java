package com.jwtauthentication.jwtauthsecurity.error;

public class JwtServiceException extends RuntimeException{
    private JwtServiceException(String message){
        super(message);
    }
}
