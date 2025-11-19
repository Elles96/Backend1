package org.oskars.Pasakums.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Step 1: Tell Spring this is a security configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Step 2: Configure security to allow all API requests (for exam/development)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Step 3: Disable CSRF protection (not needed for REST API)
                .csrf(csrf -> csrf.disable())
                // Step 4: Allow all requests to /api/** without authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // Allow all API endpoints
                        .anyRequest().authenticated() // Protect other URLs
                );

        return http.build();
    }
}