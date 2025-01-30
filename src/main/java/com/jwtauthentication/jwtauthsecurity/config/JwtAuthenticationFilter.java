package com.jwtauthentication.jwtauthsecurity.config;

import com.jwtauthentication.jwtauthsecurity.model.User;
import com.jwtauthentication.jwtauthsecurity.service.jwt.JwtService;
import com.jwtauthentication.jwtauthsecurity.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                User user = userService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, user)) {
                    List<?> rolesList = (List<?>) jwtService.extractClaim(jwt, claims -> claims.get("roles"));
                    List<?> permissionsList = (List<?>) jwtService.extractClaim(jwt,claims -> claims.get("permissions"));

                    if (rolesList != null && !rolesList.isEmpty()) {
                        List<GrantedAuthority> authorities = rolesList.stream()
                                .map(role -> new SimpleGrantedAuthority((String) role))
                                .collect(Collectors.toList());
                        if (permissionsList != null && !permissionsList.isEmpty()){
                            authorities.addAll(permissionsList.stream()
                                    .map(permission -> new SimpleGrantedAuthority((String)permission))
                                    .toList());
                        }

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(user, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
