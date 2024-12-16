package com.jwtauthentication.jwtauthsecurity.service.crudservice;

import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;

import java.util.List;

public interface CrudServiceInterface {
    List<CrudOperation> getAll();
    CrudOperation getById(int id);
    CrudOperation createPost(CrudOperation crudOperation);
    CrudOperation updatePost(CrudOperation updatedCrud,int id);
    String deletePost(int id);
    String getAuthenticatedUsername();

}
