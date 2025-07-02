package com.partizip.gateway.security;

import com.partizip.gateway.dto.AuthToken;
import com.partizip.gateway.interfaces.TokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider implements TokenProvider {
    
    private final SecretKey secretKey;
    private final long jwtExpirationInMs;
    
    public JwtTokenProvider(@Value("${jwt.secret:mySecretKey}") String secret,
                           @Value("${jwt.expiration:86400000}") long jwtExpirationInMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
    }
    
    @Override
    public AuthToken generateToken(UUID userID) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        String token = Jwts.builder()
                .setSubject(userID.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        
        LocalDateTime expiresAt = LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault());
        return new AuthToken(token, expiresAt);
    }
    
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public UUID extractUserID(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return UUID.fromString(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
