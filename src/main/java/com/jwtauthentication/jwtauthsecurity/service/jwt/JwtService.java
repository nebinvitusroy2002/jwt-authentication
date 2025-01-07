package com.jwtauthentication.jwtauthsecurity.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService implements JwtServiceInterface {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String generateToken(String username,List<String> roles){
        List<String> permissions = new ArrayList<>();
        for (String role : roles){
            permissions.addAll(getPermissionsForRole(role));
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles",roles);
        claims.put("permissions",permissions);
        return buildToken(claims,username);
    }

    public String buildToken(Map<String,Object> claims,String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractUsername(String token) {
        try{
            log.info("Extracting username from token");
            return extractClaim(token, Claims::getSubject);
        }catch (JwtException | IllegalArgumentException e){
            log.error("Error extracting username from token",e);
            throw new JwtException("Error extracting username from token",e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            log.info("Extracting claim from token");
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error extracting claim from token",e);
            throw new JwtException("Error extracting claim from token",e);
        }
    }

    public long getExpirationTime() {
        log.info("Jwt expiration time: {}",jwtExpiration);
        return jwtExpiration;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            log.info("Validating token for user: {}",userDetails.getUsername());
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }catch (JwtException | IllegalArgumentException e){
            log.error("Error validating token",e);
            throw new JwtException("Error validating token",e);
        }
    }

    private boolean isTokenExpired(String token) {
        try{
            log.info("Checking token expiration");
            return extractExpiration(token).before(new Date());
        }catch (JwtException e){
            log.error("Error checking token expiration",e);
            throw new JwtException("Error checking token expiration",e);
        }
    }

    private Date extractExpiration(String token) {
        try{
            log.info("Extracting expiration date from token");
            return extractClaim(token, Claims::getExpiration);
        }catch (JwtException | IllegalArgumentException e){
            log.error("Error extracting expiration from token",e);
            throw new JwtException("Error extracting expiration from token",e);
        }
    }

    private Claims extractAllClaims(String token) {
        try{
            log.info("Extracting all claims from token");
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            log.error("Error extracting claims from token",e);
            throw new JwtException("Error extracting claims from token",e);
        }
    }

    private Key getSignInKey() {
        try {
            log.info("Decoding the secret key");
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }catch (Exception e){
            log.error("Error decoding the secret key",e);
            throw new JwtException("Error decoding the secret key",e);
        }
    }

    public List<String> getPermissionsForRole(String role) {
        if ("ADMIN".equals(role)) {
            return List.of("READ", "WRITE", "UPDATE", "DELETE");
        } else if ("USER".equals(role)) {
            return List.of("READ","WRITE");
        } else {
            return List.of();
        }
    }
}