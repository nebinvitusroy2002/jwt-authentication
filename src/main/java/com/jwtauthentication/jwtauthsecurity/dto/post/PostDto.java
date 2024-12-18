package com.jwtauthentication.jwtauthsecurity.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {
    private String createBy;
    private String updatedBy;
    private String text;
    private LocalDateTime updatedAt;
}
