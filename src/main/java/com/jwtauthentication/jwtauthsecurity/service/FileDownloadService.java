package com.jwtauthentication.jwtauthsecurity.service;

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
//    public Resource getFileAsResource(String fileCode) throws IOException {
//        try{
//            return fileDownloadUtil.getFileAsResource(fileCode);
//        } catch (IOException e) {
//            throw new RuntimeException("File Download failed",e);
//        }
//    }
    public ResponseEntity<?> downloadFile(String fileCode) throws IOException {
        try {
            Resource resource = fileDownloadUtil.getFileAsResource(fileCode);
            if (resource == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        }catch (IOException e){
            return new ResponseEntity<>("Error while downloading the file",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
