package com.jwtauthentication.jwtauthsecurity.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class PostResponse {
    private int id;
    private String createBy;
    private String updatedBy;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
