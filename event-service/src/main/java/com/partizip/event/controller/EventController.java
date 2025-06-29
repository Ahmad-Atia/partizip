package com.partizip.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EventController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Event Service";
    }

    @GetMapping("/health")
    public String health() {
        return "Event Service is running";
    }
}
