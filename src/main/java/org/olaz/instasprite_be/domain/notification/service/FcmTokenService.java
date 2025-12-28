package org.olaz.instasprite_be.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.notification.entity.MemberFcmToken;
import org.olaz.instasprite_be.domain.notification.repository.MemberFcmTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final MemberRepository memberRepository;
    private final MemberFcmTokenRepository memberFcmTokenRepository;

    @Transactional
    public void upsert(Long memberId, String token) {
        String normalized = normalize(token);
        if (memberFcmTokenRepository.existsByMember_IdAndToken(memberId, normalized)) {
            return;
        }

        Member member = memberRepository.getReferenceById(memberId);
        memberFcmTokenRepository.save(new MemberFcmToken(member, normalized));
    }

    @Transactional
    public void delete(Long memberId, String token) {
        String normalized = normalize(token);
        memberFcmTokenRepository.deleteByMember_IdAndToken(memberId, normalized);
    }

    private static String normalize(String token) {
        return token == null ? "" : token.trim();
    }
}


