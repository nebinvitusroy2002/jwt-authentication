package com.jwtauthentication.jwtauthsecurity.service.post;

import com.jwtauthentication.jwtauthsecurity.dto.post.PostDto;
import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.Post;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.PostRepository;
import com.jwtauthentication.jwtauthsecurity.response.PostResponse;
import com.jwtauthentication.jwtauthsecurity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final MessageSource messageSource;  // Injecting MessageSource to fetch messages

    public PostResponse createPost(PostDto postDto) {
        log.info("Creating a new post");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Post post = new Post();
        post.setText(postDto.getText());
        post.setCreateBy(user.getFullName());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with ID: {}", savedPost.getId());

        return convertToPostResponse(savedPost);
    }

    public PostResponse updatePost(PostDto postDto, int id) {
        log.info("Updating post with ID: {}", id);

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post with ID {} not found for update", id);
                    return new AppException(
                            messageSource.getMessage("error.postnotfound", null, LocaleContextHolder.getLocale())
                    );
                });

        existingPost.setText(postDto.getText());
        existingPost.setUpdatedAt(LocalDateTime.now());
        existingPost.setUpdatedBy(postDto.getUpdatedBy());

        Post updatedPost = postRepository.save(existingPost);
        log.info("Post with ID {} updated successfully", id);

        return convertToPostResponse(updatedPost);
    }

    public String deletePost(int postId,User user) {
        log.info("Attempting to delete post with ID: {}", postId);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userService.findByUsername(username);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(
                        messageSource.getMessage("error.postnotfound", null, LocaleContextHolder.getLocale())
                ));

        boolean isAdmin = authenticatedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));

        if (isAdmin || post.getUser().getUserId() == authenticatedUser.getUserId()) {
            postRepository.delete(post);
            log.info("Post with ID {} deleted successfully", postId);
            return messageSource.getMessage("success.postdeleted", null, LocaleContextHolder.getLocale());
        } else {
            log.warn("User {} is not authorized to delete post {}", authenticatedUser.getUserId(), postId);
            throw new AppException(
                    messageSource.getMessage("error.accessdenied", null, LocaleContextHolder.getLocale())
            );
        }
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        log.info("Fetching posts with pagination");

        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::convertToPostResponse);
    }

    public PostResponse getPostById(int id) {
        log.info("Fetching post with ID: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        messageSource.getMessage("error.postnotfound", null, LocaleContextHolder.getLocale())
                ));

        return convertToPostResponse(post);
    }

    private PostResponse convertToPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getCreateBy(),
                post.getUpdatedBy(),
                post.getText(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
