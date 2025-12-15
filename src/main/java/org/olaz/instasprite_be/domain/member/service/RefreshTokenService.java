package org.olaz.instasprite_be.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.exception.ExpiredRefreshTokenException;
import org.olaz.instasprite_be.domain.member.exception.JwtInvalidException;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.global.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling JWT token refresh
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * Refresh access token using refresh token
     */
    @Transactional
    public JwtDto refreshToken(String refreshToken) {
        log.info("Token refresh attempt");

        // Validate refresh token
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new JwtInvalidException();
        }

        try {
            // Get member ID from refresh token
            String memberId = jwtUtil.getMemberIdFromToken(refreshToken);
            
            // Find member
            Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new JwtInvalidException());

            // Generate new tokens
            JwtDto newTokens = jwtUtil.generateTokenDto(member);
            
            log.info("Token refresh successful for member: {}", member.getUsername());
            return newTokens;

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new ExpiredRefreshTokenException();
        }
    }
}
