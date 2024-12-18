package com.jwtauthentication.jwtauthsecurity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T>{

    private String timeStamp;
    private int code;
    private boolean status;
    private String message;
    private T data;

    public ApiResponse(String message, T data, HttpStatus status){
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

}
