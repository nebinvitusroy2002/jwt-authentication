package com.jwtauthentication.jwtauthsecurity.dto.user;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private long id;
    private String name;

    private String email;
    private int statusCode;
    private String statusMsg;
    private String timeStamp;
}
