package com.jwtauthentication.jwtauthsecurity.service;

import com.jwtauthentication.jwtauthsecurity.error.ResourceNotFoundException;
import com.jwtauthentication.jwtauthsecurity.fileDownloadUtil.FileDownloadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileDownloadService {

    private final FileDownloadUtil fileDownloadUtil;

    public FileDownloadService(FileDownloadUtil fileDownloadUtil){
        this.fileDownloadUtil = fileDownloadUtil;
    }

    public ResponseEntity<Resource> downloadFile(String fileCode) throws IOException {
        try {
            Resource resource = fileDownloadUtil.getFileAsResource(fileCode);
            if (resource == null) {
                throw new ResourceNotFoundException("File not found with code: " + fileCode);
            }
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        }catch (IOException e){
            throw new IOException("Error while downloading the file: " + e.getMessage(), e);
        }
    }
}
