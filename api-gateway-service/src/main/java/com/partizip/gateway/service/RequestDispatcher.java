package com.partizip.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.http.*;

import com.partizip.gateway.dto.ApiRequest;
import com.partizip.gateway.dto.ApiResponse;

import java.util.Map;

@Service
public class RequestDispatcher {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${services.user-service.url:http://localhost:3001}")
    private String userServiceUrl;
    
    @Value("${services.event-service.url:http://localhost:3002}")
    private String eventServiceUrl;
    
    @Value("${services.community-service.url:http://localhost:3003}")
    private String communityServiceUrl;
    
    public ApiResponse forwardToUserService(ApiRequest request) {
        return forwardRequest(userServiceUrl, request);
    }
    
    public ApiResponse forwardToEventService(ApiRequest request) {
        return forwardRequest(eventServiceUrl, request);
    }
    
    public ApiResponse forwardToCommunityService(ApiRequest request) {
        return forwardRequest(communityServiceUrl, request);
    }
    
    private ApiResponse forwardRequest(String baseUrl, ApiRequest request) {
        try {
            String url = baseUrl + request.getEndpoint();
            
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            if (request.getHeaders() != null) {
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    headers.add(entry.getKey(), entry.getValue());
                }
            }
            
            // Create HTTP entity
            HttpEntity<String> httpEntity;
            if (request.getBody() != null && !request.getBody().isEmpty()) {
                httpEntity = new HttpEntity<>(request.getBody(), headers);
            } else {
                httpEntity = new HttpEntity<>(headers);
            }
            
            // Make the request
            HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, httpMethod, httpEntity, String.class);
            
            return new ApiResponse(
                responseEntity.getStatusCode().value(),
                responseEntity.getBody()
            );
            
        } catch (RestClientException ex) {
            // Handle HTTP error responses
            if (ex instanceof org.springframework.web.client.HttpStatusCodeException) {
                org.springframework.web.client.HttpStatusCodeException httpEx = 
                    (org.springframework.web.client.HttpStatusCodeException) ex;
                return new ApiResponse(httpEx.getStatusCode().value(), httpEx.getResponseBodyAsString());
            }
            return new ApiResponse(500, "Service communication error: " + ex.getMessage());
        } catch (Exception ex) {
            return new ApiResponse(500, "Internal Server Error: " + ex.getMessage());
        }
    }
}
