package com.bookit.user.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Demo-only key. In production, load from a secrets manager / env var.
    private final SecretKey key = Keys.hmacShaKeyFor(
            "change-this-to-a-real-256-bit-secret-key-before-deploying".getBytes());

    public String generateToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key)
                .compact();
    }
}
