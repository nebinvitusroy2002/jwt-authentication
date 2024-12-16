package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;
import com.jwtauthentication.jwtauthsecurity.service.crudservice.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crud")
public class CrudController {

    private final CrudService crudService;

    @GetMapping("/view")
    public List<CrudOperation> getAll(){
        return crudService.getAll();
    }

    @GetMapping("/view/{id}")
    public CrudOperation getById(@PathVariable int id){
        return crudService.getById(id);
    }

    @PostMapping("/create")
    public CrudOperation createPost(@RequestBody CrudOperation crudOperation){
        return crudService.createPost(crudOperation);
    }

    @PutMapping("/update/{id}")
    public CrudOperation updatePost(@PathVariable int id,@RequestBody CrudOperation crudOperation){
        return crudService.updatePost(id, crudOperation);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable int id){
        crudService.deletePost(id);
    }
}
