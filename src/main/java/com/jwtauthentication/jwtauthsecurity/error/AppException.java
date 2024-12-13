package com.jwtauthentication.jwtauthsecurity.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppException extends RuntimeException{
    public AppException(String message){
        super(message);
    }
}
