package com.partizip.gateway.dto;

import java.time.LocalDateTime;

public class AuthToken {
    private String value;
    private LocalDateTime expiresAt;
    
    public AuthToken() {}
    
    public AuthToken(String value, LocalDateTime expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;
    }
    
    // Getters and Setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
}
