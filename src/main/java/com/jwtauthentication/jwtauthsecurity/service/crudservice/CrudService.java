package com.jwtauthentication.jwtauthsecurity.service.crudservice;

import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;
import com.jwtauthentication.jwtauthsecurity.repository.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CrudService implements CrudServiceInterface{

    @Autowired
    private CrudRepository crudRepository;

    @Override
    public List<CrudOperation> getAll(){
      log.info("Fetching all records");
      try {
          return crudRepository.findAll();
      } catch (Exception e) {
          log.error("Error occurred while fetching all records: {}",e.getMessage());
          throw new AppException("Unable to fetch records");
      }

    }

    @Override
    public CrudOperation getById(int id){
        log.info("Fetching record with id: {}",id);
        return crudRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Record with ID {} not found",id);
                    return new AppException("Value not found");
                });
    }

    @Override
    public CrudOperation createPost(CrudOperation crudOperation){
        log.info("Creating a new record");
        try {
            CrudOperation savedOperation = crudRepository.save(crudOperation);
            log.info("Record created with ID: {}",savedOperation.getId());
            return savedOperation;
        }catch (Exception e){
            log.error("Error occurred while creating a new record: {}",e.getMessage());
            throw new AppException("Unable to create record");
        }
    }

    @Override
    public CrudOperation updatePost(CrudOperation updatedCrud,int id) {
        log.info("Updating record with ID: {}",id);
        CrudOperation existingCrud = crudRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Record with ID {} not found for update",id);
                    return new AppException("Entity not found");
                });
        existingCrud.setCreateBy(updatedCrud.getCreateBy());
        existingCrud.setUpdatedAt(updatedCrud.getUpdatedAt());
        existingCrud.setText(updatedCrud.getText());
        existingCrud.setUpdatedBy(updatedCrud.getUpdatedBy());
        try {
            log.info("Record with ID {} updated successsfully",id);
            return crudRepository.save(existingCrud);
        }catch (Exception e){
            log.error("Error occurred while updating record with ID {}: {}", id, e.getMessage());
            throw new AppException("Unable to update record");
        }
    }

    public String deletePost(int id){
        log.info("Attempting to delete record with ID: {}",id);
        if (!crudRepository.existsById(id)){
            log.warn("Record with ID {} not found for deletion", id);
            throw new AppException("User not found");
        }
        try {
            crudRepository.deleteById(id);
            log.info("Record with ID {} deleted successfully", id);
            return "Record deleted successfully..";
        }catch (Exception e){
            log.error("Error occurred while deleting record with ID {}: {}", id, e.getMessage());
            throw new AppException("Unable to delete record");
        }
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
