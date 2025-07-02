package com.partizip.gateway.dto;

import java.util.Map;

public class ApiRequest {
    private String endpoint;
    private String method;
    private Map<String, String> headers;
    private String body;
    
    public ApiRequest() {}
    
    public ApiRequest(String endpoint, String method, Map<String, String> headers, String body) {
        this.endpoint = endpoint;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }
    
    // Getters and Setters
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
