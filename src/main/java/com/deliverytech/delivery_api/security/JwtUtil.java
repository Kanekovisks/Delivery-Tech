package com.deliverytech.delivery_api.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = 
    "chave-super-secreta-para-jwt-delivery-0987654321-2026";

    private static final long EXPIRATION = 1000 * 60 * 60 * 10;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }


    public boolean isTokenValid(String token, String email) {
        try {
            Claims claims = extractClaims(token);

            return claims.getSubject().equals(email)
                    && !claims.getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }


    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
