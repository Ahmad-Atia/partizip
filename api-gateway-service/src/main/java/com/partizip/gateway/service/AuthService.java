package com.partizip.gateway.service;

import com.partizip.gateway.dto.ApiRequest;
import com.partizip.gateway.dto.AuthToken;
import com.partizip.gateway.dto.Credentials;
import com.partizip.gateway.interfaces.SecurityHandler;
import com.partizip.gateway.interfaces.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class AuthService implements SecurityHandler {
    
    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    public AuthToken authenticate(Credentials credentials) {
        // TODO: Implement actual authentication logic
        // For now, simulate authentication
        if (isValidCredentials(credentials)) {
            UUID userID = UUID.randomUUID(); // In real implementation, get from user service
            return tokenProvider.generateToken(userID);
        }
        throw new RuntimeException("Invalid credentials");
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
    
    private boolean isValidCredentials(Credentials credentials) {
        // TODO: Implement actual credential validation against user service
        // For now, simulate validation
        return credentials != null && 
               credentials.getUsername() != null && 
               credentials.getPassword() != null;
    }
}
