package com.jwtauthentication.jwtauthsecurity.repository;

import com.jwtauthentication.jwtauthsecurity.model.CrudOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudRepository extends JpaRepository<CrudOperation,Integer> {
}
