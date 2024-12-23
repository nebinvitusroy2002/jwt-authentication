package com.jwtauthentication.jwtauthsecurity.service.post;

import com.jwtauthentication.jwtauthsecurity.dto.post.PostDto;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.UserRepository;
import com.jwtauthentication.jwtauthsecurity.response.PostResponse;
import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.model.Post;
import com.jwtauthentication.jwtauthsecurity.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PostService implements PostServiceInterface {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        log.info("Fetching posts with pagination");
        try {
            Page<Post> posts = postRepository.findAll(pageable);
            log.info("Fetched posts: {}", posts.getContent());
            return posts.map(this::convertToPostResponse);
        } catch (Exception e) {
            log.error("Error while fetching posts with pagination: {}", e.getMessage());
            throw new AppException("Unable to fetch posts");
        }
    }

    @Override
    public PostResponse getPostById(int id) {
        log.info("Fetching post with ID: {}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post with ID {} not found", id);
                    return new AppException("Post not found");
                });
        return convertToPostResponse(post);
    }

    @Override
    public PostResponse createPost(PostDto postDto) {
        log.info("Creating a new post");

        String username = getAuthenticatedUsername();
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->{
                    log.error("User not found with email: {}",username);
                    return new AppException("User not found");
                });
        try {
            Post post = convertToPostEntity(postDto);
            post.setUser(user);
            post.setCreatedAt(LocalDateTime.now());
            Post savedPost = postRepository.save(post);
            log.info("Post created with ID: {}", savedPost.getId());
            return convertToPostResponse(savedPost);
        } catch (Exception e) {
            log.error("Error while creating a new post: {}", e.getMessage());
            throw new AppException("Unable to create post");
        }
    }

    @Override
    public PostResponse updatePost(PostDto postDto, int id) {
        log.info("Updating post with ID: {}", id);
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post with ID {} not found for update", id);
                    return new AppException("Post not found");
                });

        existingPost.setCreateBy(postDto.getCreateBy());
        existingPost.setUpdatedBy(postDto.getUpdatedBy());
        existingPost.setText(postDto.getText());
        existingPost.setUpdatedAt(LocalDateTime.now());

        try {
            Post updatedPost = postRepository.save(existingPost);
            log.info("Post with ID {} updated successfully", id);
            return convertToPostResponse(updatedPost);
        } catch (Exception e) {
            log.error("Error while updating post with ID {}: {}", id, e.getMessage());
            throw new AppException("Unable to update post");
        }
    }

    @Override
    public String deletePost(int postId, User user) {
        log.info("Attempting to delete post with ID: {} by user with ID: {}", postId, user.getUserId());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post with ID: {} not found", postId);
                    return new BadRequestException("Post not found");
                });

        if (user.getRole().contains("ADMIN")) {
            log.info("User with ID: {} has ADMIN role. Deleting post with ID: {}", user.getUserId(), postId);
            postRepository.delete(post);
        } else if (post.getUser().getUserId() == user.getUserId()) {
            log.info("User with ID: {} is the owner of post ID: {}. Deleting post.", user.getUserId(), postId);
            postRepository.delete(post);
        } else {
            log.warn("User with ID: {} attempted to delete post ID: {} without sufficient permissions.", user.getUserId(), postId);
            throw new AppException("Access denied");
        }
        String successMessage = String.format("Post with ID: %d successfully deleted by user with ID: %d", postId, user.getUserId());
        log.info(successMessage);
        return successMessage;
    }

    private PostResponse convertToPostResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setCreateBy(post.getCreateBy());
        response.setCreatedAt(post.getCreatedAt());
        response.setText(post.getText());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setUpdatedBy(post.getUpdatedBy());
        return response;
    }

    private Post convertToPostEntity(PostDto dto) {
        Post post = new Post();
        post.setCreateBy(dto.getCreateBy());
        post.setUpdatedBy(dto.getUpdatedBy());
        post.setText(dto.getText());
        post.setUpdatedAt(dto.getUpdatedAt());
        return post;
    }

    public String getAuthenticatedUsername() {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}

