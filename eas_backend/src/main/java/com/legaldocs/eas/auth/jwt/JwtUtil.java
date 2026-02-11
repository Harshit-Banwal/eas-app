package com.legaldocs.eas.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
	
	private static final String SECRET_STRING = "This-secret-key-is-for-JWT-token.-Written-by-Harshit-Banwal!";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h
    
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    
    public static String generateToken(UUID userId, String email) {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("email", email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(SECRET_KEY)
            .compact();
    }
    
    public static UUID extractUserId(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return UUID.fromString(claims.getSubject());
    }
}
