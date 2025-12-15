package org.olaz.instasprite_be.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Spring Security configuration for JWT-based authentication with Google OAuth support
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
        // Swagger/OpenAPI
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        
        // Authentication endpoints
        "/api/v1/auth/google/**",
        "/api/v1/auth/refresh",
        
        // Health check
        "/actuator/**",
        
        // Static image resources
        "/images/**",
        
        // Public member endpoints - use single * for path variables
        "/api/v1/accounts/*/without",
        "/api/v1/accounts/*/posts/recent/without", 
        "/api/v1/accounts/*/posts/without"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // CSRF disabled for stateless REST API
            .csrf(csrf -> csrf.disable())
            
            // Session management - stateless
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Add JWT authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers("/api/v1/accounts/*/without").permitAll()
                .requestMatchers("/api/v1/accounts/*/posts/recent/without").permitAll()
                .requestMatchers("/api/v1/accounts/*/posts/without").permitAll()
                .anyRequest().authenticated()
            )
            
            // Exception handling
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    log.warn("Unauthorized request to {}: {}", request.getRequestURI(), authException.getMessage());
                    response.sendError(401, "Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    log.warn("Access denied to {}: {}", request.getRequestURI(), accessDeniedException.getMessage());
                    response.sendError(403, "Forbidden");
                })
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",      
            "https://localhost:*",     
            "http://instasprite.duckdns.org:*",  
            "*"                        
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false); 
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

}

