package org.olaz.instasprite_be.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.exception.JwtExpiredException;
import org.olaz.instasprite_be.domain.member.exception.JwtInvalidException;
import org.olaz.instasprite_be.global.config.security.JwtAuthenticationToken;
import org.olaz.instasprite_be.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT utility class for creating and validating JWT tokens
 */
@Component
public class JwtUtil {

    private final static String CLAIM_AUTHORITIES_KEY = "authorities";
    private final static String CLAIM_JWT_TYPE_KEY = "type";
    private final static String CLAIM_MEMBER_ID_KEY = "memberId";
    private final static String BEARER_TYPE_PREFIX = "Bearer ";
    private final static String BEARER_TYPE = "Bearer";
    private final static String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final static String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final int JWT_PREFIX_LENGTH = 7;

    private final SecretKey secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtUtil(
            @Value("${jwt.secret-key}") String secret,
            @Value("${jwt.access-token-expires}") long accessTokenValidity,  // 1 hour default
            @Value("${jwt.refresh-token-expires}") long refreshTokenValidity  // 7 days default
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    /**
     * Extract JWT token from Authorization header
     */
    public String extractJwt(String authenticationHeader) {
        if (authenticationHeader == null) {
            throw new JwtInvalidException();
        } else if (!authenticationHeader.startsWith(BEARER_TYPE_PREFIX)) {
            throw new JwtInvalidException();
        }
        return authenticationHeader.substring(JWT_PREFIX_LENGTH);
    }

    /**
     * Get authentication from JWT token
     */
    public Authentication getAuthentication(String token) throws BusinessException {
        Claims claims = parseClaims(token);
        final List<SimpleGrantedAuthority> authorities = Arrays.stream(
                claims.get(CLAIM_AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        final User principal = new User((String)claims.get(CLAIM_MEMBER_ID_KEY), "", authorities);

        return new JwtAuthenticationToken(principal, token, authorities);
    }

    /**
     * Generate JWT DTO with access and refresh tokens from Authentication
     */
    public JwtDto generateTokenDto(UserDetails userDetails) {
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails.getUsername());

        return JwtDto.builder()
                .type("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Generate JWT DTO with access and refresh tokens from Member
     */
    public JwtDto generateTokenDto(Member member) {
        final String authoritiesString = member.getRole().toString();
        long currentTime = (new Date()).getTime();

        final Date accessTokenExpiresIn = new Date(currentTime + accessTokenValidity);
        final Date refreshTokenExpiresIn = new Date(currentTime + refreshTokenValidity);

        final String accessToken = Jwts.builder()
            .subject(ACCESS_TOKEN_SUBJECT)
            .claim(CLAIM_MEMBER_ID_KEY, member.getId().toString())
            .claim(CLAIM_AUTHORITIES_KEY, authoritiesString)
            .claim(CLAIM_JWT_TYPE_KEY, BEARER_TYPE)
            .expiration(accessTokenExpiresIn)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();

        final String refreshToken = Jwts.builder()
            .subject(REFRESH_TOKEN_SUBJECT)
            .claim(CLAIM_MEMBER_ID_KEY, member.getId().toString())
            .claim(CLAIM_AUTHORITIES_KEY, authoritiesString)
            .expiration(refreshTokenExpiresIn)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();

        return JwtDto.builder()
            .type(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .name(member.getName())
            .username(member.getUsername())
            .email(member.getEmail())
            .build();
    }

    /**
     * Generate access token
     */
    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidity);

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .claim(CLAIM_MEMBER_ID_KEY, userDetails.getUsername())
                .claim(CLAIM_AUTHORITIES_KEY, authorities)
                .claim(CLAIM_JWT_TYPE_KEY, BEARER_TYPE)
                .expiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .claim(CLAIM_MEMBER_ID_KEY, username)
                .expiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Parse and validate JWT claims
     */
    private Claims parseClaims(String token) throws BusinessException {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException();
        } catch (Exception e) {
            throw new JwtInvalidException();
        }
    }

    /**
     * Validate if token is a refresh token
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return REFRESH_TOKEN_SUBJECT.equals(claims.getSubject());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get member ID from token
     */
    public String getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return (String) claims.get(CLAIM_MEMBER_ID_KEY);
    }
}

