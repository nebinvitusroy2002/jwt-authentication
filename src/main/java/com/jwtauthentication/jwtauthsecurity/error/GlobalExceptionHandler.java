package com.jwtauthentication.jwtauthsecurity.error;

import com.jwtauthentication.jwtauthsecurity.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<UserResponseDto> handleBadRequestException(BadRequestException e){
        String localizedMessage = messageSource.getMessage(
                "error.badrequest",
                null,
                LocaleContextHolder.getLocale()
        );
        UserResponseDto errorResponse = UserResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusMsg(localizedMessage)
                .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponseDto>  handleGeneralException(Exception e){
        String localizedMessage = messageSource.getMessage(
                "error.unexpected",
                null,
                LocaleContextHolder.getLocale()
        );
        UserResponseDto errorResponse = UserResponseDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMsg(localizedMessage)
                .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<UserResponseDto> handleAppException(AppException e){
        String localizedMessage = messageSource.getMessage(
                "error.appexception",
                null,
                LocaleContextHolder.getLocale()
        );
        UserResponseDto errorResponse = UserResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusMsg(localizedMessage)
                .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        String localizedMessage = messageSource.getMessage(
                "error.fileio",
                null,
                LocaleContextHolder.getLocale()
        );
        return new ResponseEntity<>(localizedMessage+":"+ ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
