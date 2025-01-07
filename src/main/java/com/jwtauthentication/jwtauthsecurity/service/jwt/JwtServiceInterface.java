package com.jwtauthentication.jwtauthsecurity.service.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface JwtServiceInterface {
     String extractUsername(String token);
     <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
     String generateToken(String username,List<String> roles);
     long getExpirationTime();
     boolean isTokenValid(String token, UserDetails userDetails);
}
