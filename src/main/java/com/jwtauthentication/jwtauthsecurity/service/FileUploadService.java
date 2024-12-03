package com.jwtauthentication.jwtauthsecurity.service;

import com.jwtauthentication.jwtauthsecurity.fileUploadUtil.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileUploadService {

    public String saveFile(MultipartFile multipartFile){
        try{
            return FileUploadUtil.saveFile(multipartFile.getOriginalFilename(),multipartFile);
        }catch (IOException e){
            throw new RuntimeException("File upload failed",e);
        }
    }
}
