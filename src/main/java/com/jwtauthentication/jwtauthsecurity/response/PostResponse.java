package com.jwtauthentication.jwtauthsecurity.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private long id;
    private String createBy;
    private String updatedBy;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
