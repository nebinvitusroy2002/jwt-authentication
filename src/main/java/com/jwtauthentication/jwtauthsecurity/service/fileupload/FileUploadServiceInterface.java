package com.jwtauthentication.jwtauthsecurity.service.fileupload;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadServiceInterface {
     String saveFile(MultipartFile multipartFile) throws AppException;
}
