package org.olaz.instasprite_be.global.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    @Value("${SWAGGER_USERNAME:admin}")
    private String swaggerUsername;

    @Value("${SWAGGER_PASSWORD:admin}")
    private String swaggerPassword;

    private static final String[] SWAGGER_ENDPOINTS = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**"
    };

    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/v1/auth/google/**",
        "/api/v1/auth/refresh",
        "/api/v1/auth/login",
        "/api/v1/auth/register",
        "/api/v1/auth/email/verify",
        "/api/v1/auth/password/forgot",
        "/api/v1/auth/password/reset",

        "/actuator/**",

        "/images/**",

        "/api/v1/accounts/*/without",
        "/api/v1/accounts/*/posts/recent/without", 
        "/api/v1/accounts/*/posts/without"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain for Swagger UI endpoints with HTTP Basic Authentication
     * This runs first (Order 1) to protect Swagger before the main security chain
     */
    @Bean
    @Order(1)
    public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http, UserDetailsService swaggerUserDetailsService) throws Exception {
        http
            .securityMatcher(SWAGGER_ENDPOINTS)
            .userDetailsService(swaggerUserDetailsService)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * User details service for Swagger Basic Auth
     */
    @Bean
    public UserDetailsService swaggerUserDetailsService() {
        UserDetails user = User.builder()
            .username(swaggerUsername)
            .password(passwordEncoder().encode(swaggerPassword))
            .roles("SWAGGER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @Order(2)
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
            "http://euler.olaz.io.vn:*",
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

