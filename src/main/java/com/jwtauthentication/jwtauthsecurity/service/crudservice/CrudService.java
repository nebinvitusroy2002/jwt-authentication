package com.jwtauthentication.jwtauthsecurity.service.crudservice;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;
import com.jwtauthentication.jwtauthsecurity.repository.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrudService implements CrudServiceInterface{

    @Autowired
    private CrudRepository crudRepository;

    public List<CrudOperation> getAll(){
        return crudRepository.findAll();
    }

    public CrudOperation getById(int id){
        return crudRepository.findById(id)
                .orElseThrow(()->new AppException("Value not found"));
    }

    public CrudOperation createPost(CrudOperation crudOperation){
        return crudRepository.save(crudOperation);
    }

    public CrudOperation updatePost(int id, CrudOperation crudDetails){
        CrudOperation crudOperation = getById(id);
        if (crudDetails.getUpdatedBy() == null || crudDetails.getUpdatedAt() == null){
            throw new AppException("UpdatedBy and UpdatedAt cannot be null");
        }
        crudOperation.setText(crudDetails.getText());
        crudOperation.setUpdatedBy(crudDetails.getUpdatedBy());
        crudOperation.setUpdatedAt(crudDetails.getUpdatedAt());
        return crudRepository.save(crudOperation);
    }

    public void deletePost(int id){
        if (!crudRepository.existsById(id)){
            throw new AppException("User not found");
        }
        crudRepository.deleteById(id);
    }

    public String getAuthenticatedUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }else {
            return principal.toString();
        }
    }
}
