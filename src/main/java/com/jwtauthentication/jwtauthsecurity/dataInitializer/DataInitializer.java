package com.jwtauthentication.jwtauthsecurity.dataInitializer;

import com.jwtauthentication.jwtauthsecurity.model.Permission;
import com.jwtauthentication.jwtauthsecurity.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args)throws Exception{
        if(permissionRepository.count() == 0){
            Permission readPermission = new Permission("READ");
            Permission writePermission = new Permission("WRITE");
            Permission updatePermission = new Permission("UPDATE");
            Permission deletePermission = new Permission("DELETE");

            permissionRepository.saveAll(Arrays.asList(readPermission,writePermission,updatePermission,deletePermission));
            System.out.println("Permissions initialized successfully");
        }
    }
}
