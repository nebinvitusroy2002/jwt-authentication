package com.jwtauthentication.jwtauthsecurity.service.post;

import com.jwtauthentication.jwtauthsecurity.dto.post.PostDto;
import com.jwtauthentication.jwtauthsecurity.model.Post;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceInterface {
    Page<PostResponse> getAllPosts(Pageable pageableable);
    PostResponse getPostById(long id);
    PostResponse createPost(PostDto postDto);
    PostResponse updatePost(PostDto postDto, long id);
    String deletePost(long postId, String token);
    private boolean hasPermission(String requiredPermission) {
        return false;
    }
}
