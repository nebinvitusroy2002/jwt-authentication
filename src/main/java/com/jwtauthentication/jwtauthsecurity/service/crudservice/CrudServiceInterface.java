package com.jwtauthentication.jwtauthsecurity.service.crudservice;

import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;

import java.util.List;

public interface CrudServiceInterface {
    List<CrudOperation> getAll();
    CrudOperation getById(int id);
    CrudOperation createPost(CrudOperation crudOperation);
    CrudOperation updatePost(int id, CrudOperation crudDetails);
    void deletePost(int id);
    String getAuthenticatedUsername();

}
