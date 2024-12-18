package com.jwtauthentication.jwtauthsecurity.controller;

import com.jwtauthentication.jwtauthsecurity.dto.post.PostDto;
import com.jwtauthentication.jwtauthsecurity.response.PostResponse;
import com.jwtauthentication.jwtauthsecurity.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;



    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> postsPage = postService.getAllPosts(PageRequest.of(page, size));
        List<Map<String, Object>> postData = postsPage.getContent().stream()
                .map(this::extractPostData)
                .collect(Collectors.toList());
        Map<String, Object> paginationDetails = new LinkedHashMap<>();
        paginationDetails.put("currentPage", postsPage.getNumber());
        paginationDetails.put("totalPages", postsPage.getTotalPages());
        paginationDetails.put("totalItems", postsPage.getTotalElements());
        paginationDetails.put("pageSize", postsPage.getSize());
    
        Map<String, Object> response = createResponse("Posts retrieved successfully", postData);
        response.put("pagination", paginationDetails);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable int id) {
        PostResponse post = postService.getPostById(id);
        Map<String, Object> postData = extractPostData(post);

        return ResponseEntity.ok(createResponse("Post retrieved successfully", postData));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody PostDto postDto) {
        PostResponse createdPost = postService.createPost(postDto);
        Map<String, Object> postData = extractPostData(createdPost);

        return new ResponseEntity<>(createResponse("Post created successfully", postData), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable int id, @RequestBody PostDto postDto) {
        PostResponse updatedPost = postService.updatePost(postDto, id);
        Map<String, Object> postData = extractPostData(updatedPost);

        return ResponseEntity.ok(createResponse("Post updated successfully", postData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable int id) {
        String message = postService.deletePost(id);

        return ResponseEntity.ok(createResponse(message, null));
    }

    private Map<String, Object> createResponse(String message, Object data) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("code", HttpStatus.OK.value());
        response.put("status", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> extractPostData(PostResponse postResponse) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", postResponse.getId());
        data.put("createBy", postResponse.getCreateBy());
        data.put("createdAt", postResponse.getCreatedAt());
        data.put("text", postResponse.getText());
        data.put("updatedAt", postResponse.getUpdatedAt());
        data.put("updatedBy", postResponse.getUpdatedBy());
        return data;
    }
}
