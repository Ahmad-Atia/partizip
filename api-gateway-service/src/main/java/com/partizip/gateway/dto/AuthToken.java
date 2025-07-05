package com.partizip.gateway.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuthToken {
    private String value;
    private LocalDateTime expiresAt;
    private UUID userID;
    private boolean expired;
    
    public AuthToken() {}
    
    public AuthToken(String value, LocalDateTime expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;
        this.expired = false;
    }
    
    public AuthToken(String value, LocalDateTime expiresAt, UUID userID) {
        this.value = value;
        this.expiresAt = expiresAt;
        this.userID = userID;
        this.expired = false;
    }
    
    // Getters and Setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public UUID getUserID() { return userID; }
    public void setUserID(UUID userID) { this.userID = userID; }
    
    public boolean isExpired() {
        if (expired) return true;
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public void setExpired(boolean expired) { this.expired = expired; }
}
