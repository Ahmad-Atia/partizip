package com.partizip.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.partizip.gateway.interfaces.TokenProvider;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationGatewayFilterFactory.class);

    @Autowired
    private TokenProvider tokenProvider;

    public JwtAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();
            
            logger.debug("Processing request: {}", path);

            // Skip authentication for public endpoints
            if (isPublicEndpoint(path)) {
                logger.debug("Public endpoint, skipping authentication: {}", path);
                return chain.filter(exchange);
            }

            // Check for Authorization header
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("Missing or invalid Authorization header for: {}", path);
                return handleUnauthorized(exchange);
            }

            // Validate JWT token
            String token = authHeader.substring(7);
            if (!tokenProvider.validateToken(token)) {
                logger.warn("Invalid JWT token for: {}", path);
                return handleUnauthorized(exchange);
            }

            logger.debug("Request authenticated successfully: {}", path);
            return chain.filter(exchange);
        };
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/hello") || 
               path.equals("/api/health") || 
               path.equals("/api/auth/login") ||
               path.startsWith("/actuator");
    }

    private Mono<Void> handleUnauthorized(org.springframework.web.server.ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }
}