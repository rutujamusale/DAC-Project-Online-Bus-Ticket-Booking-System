package com.bus_ticket.filter;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    // Secret key for signing JWTs (read from application.properties or default if missing)
    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;

    // Token validity duration in milliseconds (default: 24 hours)
    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private Key key;

    // Generate the secret key after the bean is initialized
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate a JWT token with user email and role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email) // Subject of the token (usually username or email)
                .claim("role", role) // Custom claim to store user role
                .setIssuedAt(new Date()) // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Expiry time
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token with HMAC SHA256
                .compact(); // Create the token
    }

    // Validate the token and return Authentication object if valid
    public Authentication validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            // Create authority from the role stored in token
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
            );

            // Return Spring Security's authentication token
            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (Exception e) {
            // Token is invalid or expired
            return null;
        }
    }

    // Extract user email (subject) from token
    public String extractEmail(String token) {
        try {
            return parseToken(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // Extract role from token
    public String extractRole(String token) {
        try {
            return parseToken(token).get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Private helper method to parse JWT and return claims
    private Claims parseToken(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        return parser.parseClaimsJws(token).getBody();
    }
}
