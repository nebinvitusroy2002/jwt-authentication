package com.jwtauthentication.jwtauthsecurity.fileDownloadUtil;


import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class FileDownloadUtil {
    private Path foundFile;
    public UrlResource getFileAsResource(String fileCode) throws IOException{
        Path dirPath = Paths.get("Files-Upload");
        try(Stream<Path> stream = Files.list(dirPath)) {
            stream.forEach(file -> {
                if (file.getFileName().toString().startsWith(fileCode)) {
                    foundFile = file;
                }
            });
        }catch (IOException e){
            throw new IOException("Error reading files from directory: "+dirPath,e);
        }
        if (foundFile != null){
            return new UrlResource(foundFile.toUri());
        }
        return null;
    }
}
