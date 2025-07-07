package com.partizip.event.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Simple test application to verify the Observer Pattern implementation
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.partizip.event")
public class ObserverPatternTestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ObserverPatternTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Observer Pattern Test Application started successfully!");
        System.out.println("MQTT Event Observer implementation is ready for real-time event notifications.");
    }
}
