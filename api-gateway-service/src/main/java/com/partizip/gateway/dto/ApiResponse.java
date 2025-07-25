package com.partizip.gateway.dto;

public class ApiResponse {
    private int statusCode;
    private String body;
    
    public ApiResponse() {}
    
    public ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
    
    // Getters and Setters
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
