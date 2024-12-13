package com.jwtauthentication.jwtauthsecurity.service.filedownload;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.fileDownloadUtil.FileDownloadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileDownloadService implements FileDownloadServiceInterface {

    private final FileDownloadUtil fileDownloadUtil;

    public ResponseEntity<Resource> downloadFile(String fileCode) throws IOException {
        try {
            log.info("Attempting to downloadfile with code: {}",fileCode);
            Resource resource = fileDownloadUtil.getFileAsResource(fileCode);
            if (resource == null) {
                log.error("File not found with code: {}",fileCode);
                throw new AppException("File not found with code: " + fileCode);
            }
            log.info("Successfully retrieved file: {}",resource.getFilename());
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        }catch (IOException e){
            log.error("Error occurred while downloading the file: {}", e.getMessage());
            throw new IOException("Error while downloading the file: " + e.getMessage(), e);
        }catch (AppException e) {
            log.error("Application-specific error: {}", e.getMessage());
            throw e;
        }
    }
}
