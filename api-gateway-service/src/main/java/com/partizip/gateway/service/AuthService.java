package com.partizip.gateway.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.partizip.gateway.dto.ApiRequest;
import com.partizip.gateway.dto.AuthToken;
import com.partizip.gateway.dto.Credentials;
import com.partizip.gateway.interfaces.SecurityHandler;
import com.partizip.gateway.interfaces.TokenProvider;

import reactor.core.publisher.Mono;

@Service
public class AuthService implements SecurityHandler {
    
    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    public Mono<AuthToken> authenticate(Credentials credentials) {
        return authenticateWithUserService(credentials)
            .map(userID -> {
                if (userID != null) {
                    return tokenProvider.generateToken(userID);
                }
                throw new RuntimeException("Invalid credentials");
            })
            .onErrorMap(e -> new RuntimeException("Authentication failed: " + e.getMessage()));
    }
    
    private Mono<UUID> authenticateWithUserService(Credentials credentials) {
        // Create request body for user service
        UserServiceCredentialsRequest request = new UserServiceCredentialsRequest();
        request.setEmail(credentials.getUsername());
        request.setPassword(credentials.getPassword());
        
        // Call user service internal endpoint - non-blocking
        WebClient webClient = webClientBuilder.build();
        return webClient
            .post()
            .uri("http://user-service:3001/internal/verify-credentials")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserServiceCredentialsResponse.class)
            .map(response -> {
                if (response != null && response.isValid()) {
                    return UUID.fromString(response.getUserId());
                }
                return null;
            })
            .onErrorResume(e -> {
                System.err.println("Failed to authenticate with user service: " + e.getMessage());
                return Mono.just(null);
            });
    }
    
    public boolean authorize(AuthToken token, String resource) {
        if (token == null || token.getValue() == null) {
            return false;
        }
        
        if (token.isExpired()) {
            return false;
        }
        
        return tokenProvider.validateToken(token.getValue());
    }
    
    @Override
    public AuthToken authenticate(ApiRequest request) {
        // Extract credentials from request headers or body
        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenProvider.validateToken(token)) {
                UUID userID = tokenProvider.extractUserID(token);
                return tokenProvider.generateToken(userID);
            }
        }
        
        // If no valid token, attempt to authenticate with credentials
        // This would typically be extracted from the request body
        // For now, return null to indicate authentication failure
        return null;
    }
    
    @Override
    public boolean authorize(ApiRequest request) {
        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return tokenProvider.validateToken(token);
        }
        return false;
    }
    
    // DTO classes for User service communication
    public static class UserServiceCredentialsRequest {
        private String email;
        private String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class UserServiceCredentialsResponse {
        private String userId;
        private String email;
        private boolean valid;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
    }
}
