package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;
import com.jwtauthentication.jwtauthsecurity.service.crudservice.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CrudOperation> createPost(@RequestBody CrudOperation crudOperation){
        return new ResponseEntity<CrudOperation>(crudService.createPost(crudOperation),HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CrudOperation> updatePost(@PathVariable int id,@RequestBody CrudOperation crudOperation){
        return new ResponseEntity<CrudOperation>(crudService.updatePost(crudOperation,id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id){
        String message = crudService.deletePost(id);
        return ResponseEntity.ok(message);
    }
}
