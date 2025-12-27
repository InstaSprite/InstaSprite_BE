package org.olaz.instasprite_be.global.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.exception.JwtExpiredException;
import org.olaz.instasprite_be.domain.member.exception.JwtInvalidException;
import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;
import org.olaz.instasprite_be.global.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Validates JWT tokens on each request
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        boolean isPublic = isPublicEndpoint(requestURI);
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (token != null && !jwtUtil.isRefreshToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT authentication successful for user: {}", authentication.getName());
            }
            
        } catch (JwtExpiredException e) {
            if (!isPublic) {
                log.warn("JWT token expired for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.JWT_EXPIRED.getMessage());
                return;
            }
        } catch (JwtInvalidException e) {
            if (!isPublic) {
                log.warn("Invalid JWT token for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.JWT_INVALID.getMessage());
                return;
            }
        } catch (BusinessException e) {
            if (!isPublic) {
                log.warn("JWT authentication failed for request: {} - {}", request.getRequestURI(), e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isPublicEndpoint(String requestURI) {
        return (requestURI.startsWith("/api/v1/accounts/") && requestURI.endsWith("/without")) ||
               (requestURI.startsWith("/api/v1/accounts/") && requestURI.contains("/posts/recent/without")) ||
               (requestURI.startsWith("/api/v1/accounts/") && requestURI.contains("/posts/without")) ||
               requestURI.startsWith("/api/v1/auth/") ||
               requestURI.startsWith("/v3/api-docs/") ||
               requestURI.startsWith("/swagger-ui") ||
               requestURI.startsWith("/actuator/");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                return jwtUtil.extractJwt(authorizationHeader);
            } catch (Exception e) {
                return null;
            }
        }
        
        return null;
    }
}
