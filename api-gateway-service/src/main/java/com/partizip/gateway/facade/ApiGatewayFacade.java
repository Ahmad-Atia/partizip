package com.partizip.gateway.facade;

import com.partizip.gateway.dto.ApiRequest;
import com.partizip.gateway.dto.ApiResponse;
import com.partizip.gateway.service.AuthService;
import com.partizip.gateway.service.MqttMessageHandler;
import com.partizip.gateway.service.RequestDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiGatewayFacade {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private RequestDispatcher dispatcher;
    
    @Autowired
    private MqttMessageHandler mqttHandler;
    
    public ApiResponse handleRequest(ApiRequest request) {
        // Check if the endpoint requires authentication
        if (requiresAuthentication(request.getEndpoint())) {
            if (!authService.authorize(request)) {
                return new ApiResponse(401, "Unauthorized");
            }
        }
        
        // Route the request to the appropriate service
        return routeRequest(request);
    }
    
    private ApiResponse routeRequest(ApiRequest request) {
        String endpoint = request.getEndpoint();
        
        if (endpoint.startsWith("/users") || endpoint.startsWith("/auth")) {
            return dispatcher.forwardToUserService(request);
        } else if (endpoint.startsWith("/events")) {
            return dispatcher.forwardToEventService(request);
        } else if (endpoint.startsWith("/communities")) {
            return dispatcher.forwardToCommunityService(request);
        } else {
            return new ApiResponse(404, "Endpoint not found");
        }
    }
    
    private boolean requiresAuthentication(String endpoint) {
        // Define public endpoints that don't require authentication
        return !endpoint.equals("/auth/login") && 
               !endpoint.equals("/auth/register") &&
               !endpoint.equals("/health") &&
               !endpoint.equals("/hello");
    }
    
    public void publishNotification(String topic, String message) {
        mqttHandler.publish(topic, message);
    }
    
    public void subscribeToNotifications(String topic) {
        mqttHandler.subscribe(topic);
    }
}
