package com.jwtauthentication.jwtauthsecurity.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileUploadResponse {
    private String fileName;

    private String downloadUrl;
    private long size;
}

