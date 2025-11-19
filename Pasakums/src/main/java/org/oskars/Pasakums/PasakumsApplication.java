package org.oskars.Pasakums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Step 1: This tells Spring Boot to auto-configure everything
@SpringBootApplication
public class PasakumsApplication {

    // Step 2: Main method - entry point of the application
    public static void main(String[] args) {
        // Step 3: Start the Spring Boot application
        SpringApplication.run(PasakumsApplication.class, args);
    }
}