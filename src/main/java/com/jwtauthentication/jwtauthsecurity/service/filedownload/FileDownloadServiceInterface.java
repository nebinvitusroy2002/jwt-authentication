package com.jwtauthentication.jwtauthsecurity.service.filedownload;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileDownloadServiceInterface {
     ResponseEntity<Resource> downloadFile(String fileCode) throws IOException;
}
