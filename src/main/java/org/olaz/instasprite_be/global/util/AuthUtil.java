package org.olaz.instasprite_be.global.util;

import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Authentication utility class for retrieving the current logged-in member
 */
@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final MemberRepository memberRepository;

    /**
     * Get the current logged-in member's ID
     * @return logged-in member's ID
     */
    public Long getLoginMemberId() {
        return getLoginMember().getId();
    }

    /**
     * Get the current logged-in member entity
     * @return logged-in Member entity
     * @throws BusinessException if authentication fails or member not found
     */
    public Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAIL);
        }

        try {
            Long memberId = Long.valueOf(authentication.getName());
            return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}





