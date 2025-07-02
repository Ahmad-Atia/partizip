package com.partizip.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partizip.gateway.dto.AuthToken;
import com.partizip.gateway.dto.Credentials;
import com.partizip.gateway.service.AuthService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiGatewayController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello from API Gateway!");
    }
    
    @GetMapping("/health")
    public Mono<String> health() {
        return Mono.just("API Gateway is running");
    }
    
    @PostMapping("/auth/login")
    public Mono<ResponseEntity<AuthToken>> login(@RequestBody Credentials credentials) {
        try {
            AuthToken token = authService.authenticate(credentials);
            return Mono.just(ResponseEntity.ok(token));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(401).build());
        }
    }
}
