package com.jwtauthentication.jwtauthsecurity.repository;

import com.jwtauthentication.jwtauthsecurity.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
