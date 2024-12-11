package com.jwtauthentication.jwtauthsecurity.service;

import com.jwtauthentication.jwtauthsecurity.error.FileUploadException;
import com.jwtauthentication.jwtauthsecurity.fileUploadUtil.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadService {

    public String saveFile(MultipartFile multipartFile) throws FileUploadException {
        try{
            if (multipartFile == null || multipartFile.isEmpty()){
                throw new FileUploadException("Uploaded file is empty or missing");
            }
            String originalFilename = multipartFile.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                throw new FileUploadException("File name cannot be empty or null");
            }
            return FileUploadUtil.saveFile(originalFilename,multipartFile);
        }catch (IOException e){
            throw new FileUploadException("File upload failed due to an I/O error", e);
        }
    }
}
