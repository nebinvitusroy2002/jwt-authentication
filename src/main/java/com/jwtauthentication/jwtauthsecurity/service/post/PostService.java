package com.jwtauthentication.jwtauthsecurity.service.post;

import com.jwtauthentication.jwtauthsecurity.dto.post.PostDto;
import com.jwtauthentication.jwtauthsecurity.error.AppException;
import com.jwtauthentication.jwtauthsecurity.error.BadRequestException;
import com.jwtauthentication.jwtauthsecurity.model.Post;
import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.repository.PostRepository;
import com.jwtauthentication.jwtauthsecurity.response.PostResponse;
import com.jwtauthentication.jwtauthsecurity.service.jwt.JwtService;
import com.jwtauthentication.jwtauthsecurity.service.user.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService implements PostServiceInterface{

    private final PostRepository postRepository;
    private final UserService userService;
    private final MessageSource messageSource;
    private final JwtService jwtService;

    private boolean hasPermission(String requiredPermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        List<String> userPermissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return userPermissions.contains(requiredPermission);
    }


    public PostResponse createPost(PostDto postDto) {
        if (hasPermission("CREATE")) {
            throw new AppException("Access denied. You do not have permission to create posts.");
        }
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

    public PostResponse updatePost(PostDto postDto, long id) {
        if (hasPermission("UPDATE")) {
            throw new AppException("Access denied. You do not have permission to update posts.");
        }

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Post with ID {} not found for update", id);
                    return new AppException(
                            messageSource.getMessage("error.postnotfound", null, LocaleContextHolder.getLocale())
                    );
                });

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        existingPost.setText(postDto.getText());
        existingPost.setUpdatedAt(LocalDateTime.now());
        existingPost.setUpdatedBy(user.getFullName());

        Post updatedPost = postRepository.save(existingPost);
        log.info("Post with ID {} updated successfully", id);

        return convertToPostResponse(updatedPost);
    }

    public String deletePost(long postId, String token) {
        if (hasPermission("DELETE")) {
            throw new AppException("Access denied. You do not have permission to delete posts.");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(
                        messageSource.getMessage("error.postnotfound", null, LocaleContextHolder.getLocale())
                ));

        Object rolesClaim = jwtService.extractClaim(token, claims -> claims.get("roles"));
        List<String> roles;
        if (rolesClaim instanceof List<?>) {
            roles = ((List<?>) rolesClaim).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        } else {
            throw new JwtException("Roles claim is not in expected format.");
        }
        boolean isAdmin = roles.contains("ADMIN");

        if (isAdmin) {
            postRepository.delete(post);
            log.info("Post with ID {} deleted successfully", postId);
            return messageSource.getMessage("success.postdeleted", null, LocaleContextHolder.getLocale());
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userService.findByUsername(username);
        if (post.getUser().getUserId() == authenticatedUser.getUserId()) {
            postRepository.delete(post);
            log.info("Post with ID {} deleted successfully by user", postId);
            return messageSource.getMessage("success.postdeleted", null, LocaleContextHolder.getLocale());
        }else {
            log.warn("User {} is not authorized to delete post {}", authenticatedUser.getUserId(), postId);
            throw new AppException(
                    messageSource.getMessage("error.accessdenied", null, LocaleContextHolder.getLocale())
            );
        }
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        if (hasPermission("READ")) {
            throw new AppException("Access denied. You do not have permission to view posts.");
        }
        return postRepository.findAll(pageable).map(this::convertToPostResponse);
    }


    public PostResponse getPostById(long id) {
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
