package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.dto.post.PostDto;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.response.PostResponse;
import com.jwtauthentication.jwtauthsecurity.service.post.PostService;
import com.jwtauthentication.jwtauthsecurity.response.ApiResponse;
import com.jwtauthentication.jwtauthsecurity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> postsPage = postService.getAllPosts(PageRequest.of(page, size));
        ApiResponse<Page<PostResponse>> response = new ApiResponse<>(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.OK.value(),
                true,
                "Posts retrieved successfully",
                postsPage);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable int id) {
        PostResponse post = postService.getPostById(id);
        ApiResponse<PostResponse> response = new ApiResponse<>(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.OK.value(),
                true,
                "Post retrieved successfully",

                post);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostDto postDto) {
        PostResponse createdPost = postService.createPost(postDto);
        ApiResponse<PostResponse> response = new ApiResponse<>(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.CREATED.value(),
                true,
                "Post created successfully",
                createdPost);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable int id, @RequestBody PostDto postDto) {
        PostResponse updatedPost = postService.updatePost(postDto, id);
        ApiResponse<PostResponse> response = new ApiResponse<>(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.OK.value(),
                true,
                "Post updated successfully",
                updatedPost);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable long id, @RequestHeader("Authorization")String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer")?authorizationHeader.substring(7):authorizationHeader;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userService.findByUsername(username);
        String message = postService.deletePost(id, token);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.OK.value(),
                true,
                message,
                null);
        return ResponseEntity.ok(response);
    }
}