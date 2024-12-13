package com.jwtauthentication.jwtauthsecurity.error;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
