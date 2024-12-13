package com.jwtauthentication.jwtauthsecurity.service.fileupload;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.fileUploadUtil.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class FileUploadService implements FileUploadServiceInterface {

    public String saveFile(MultipartFile multipartFile) throws AppException {
        try{
            log.info("Attempting to upload file: {}",multipartFile.getOriginalFilename());
            if (multipartFile.isEmpty()){
                log.error("Uploaded file is empty or missing");
                throw new AppException("Uploaded file is empty or missing");
            }
            String originalFilename = multipartFile.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                log.error("File name cannot be empty or null");
                throw new AppException("File name cannot be empty or null");
            }
            log.info("File validation successful. Proceeding to save file: {}",originalFilename);
            return FileUploadUtil.saveFile(originalFilename,multipartFile);
        }catch (IOException e){
            log.error("File upload failed due to an I/O error: {}", e.getMessage());
            throw new AppException("File upload failed due to an I/O error");
        } catch (AppException e) {
            log.error("Application-specific error: {}", e.getMessage());
            throw e;
        }
    }
}
