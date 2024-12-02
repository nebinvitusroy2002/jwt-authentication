package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.fileDownloadUtil.FileDownloadUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/download")
@RestController
public class FileDownloadController {

    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {
        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        org.springframework.core.io.Resource resource = null;
        try {
            resource = downloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename= \"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}

