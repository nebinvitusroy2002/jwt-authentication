package com.jwtauthentication.jwtauthsecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        try{
            return extractClaim(token, Claims::getSubject);
        }catch (JwtException | IllegalArgumentException e){
            throw new RuntimeException("Error extracting username from token",e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Error extracting claim from token",e);
        }
    }
    public String generateToken(UserDetails userDetails) {
        try{
            return generateToken(new HashMap<>(), userDetails);
        }catch (JwtException e){
            throw new RuntimeException("Error generating token",e);
        }
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try{
            return buildToken(extraClaims, userDetails, jwtExpiration);
        }catch (JwtException e){
            throw new RuntimeException("Error generating token with extra claims",e);
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (JwtException e){
            throw new RuntimeException("Error building token",e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        }catch (JwtException | IllegalArgumentException e){
            throw new RuntimeException("Error validating token",e);
        }
    }

    private boolean isTokenExpired(String token) {
        try{
            return extractExpiration(token).before(new Date());
        }catch (JwtException e){
            throw new RuntimeException("Error checking token expiration",e);
        }
    }

    private Date extractExpiration(String token) {
        try{
            return extractClaim(token, Claims::getExpiration);
        }catch (JwtException | IllegalArgumentException e){
            throw new RuntimeException("Error extracting expiration from token",e);
        }
    }

    private Claims extractAllClaims(String token) {
        try{
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            throw new RuntimeException("Error extracting claims from token",e);
        }
    }

    private Key getSignInKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }catch (Exception e){
            throw new RuntimeException("Error decoding the secret key",e);
        }
    }
}