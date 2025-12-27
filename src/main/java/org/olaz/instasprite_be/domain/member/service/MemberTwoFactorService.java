package org.olaz.instasprite_be.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.exception.*;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberTwoFactorService {

    private final MemberRepository memberRepository;
    private final TotpService totpService;

    @Transactional(readOnly = true)
    public boolean isTotpEnabled(Long memberId) {
        return memberRepository.findById(memberId)
                .map(Member::isTotpEnabled)
                .orElseThrow(MemberDoesNotExistException::new);
    }

    @Transactional
    public TotpService.Enrollment enrollTotp(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberDoesNotExistException::new);
        if (member.isTotpEnabled()) {
            throw new TotpAlreadyEnabledException();
        }

        TotpService.Enrollment enrollment = totpService.enroll(member);
        memberRepository.save(member);
        return enrollment;
    }

    @Transactional
    public void enableTotp(Long memberId, String otpCode) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberDoesNotExistException::new);
        if (member.isTotpEnabled()) {
            throw new TotpAlreadyEnabledException();
        }
        if (member.getTotpSecret() == null) {
            throw new TotpNotEnrolledException();
        }

        if (!totpService.verifyMemberCode(member, otpCode)) {
            throw new TotpInvalidCodeException();
        }

        member.enableTotp();
        memberRepository.save(member);
    }

    @Transactional
    public void disableTotp(Long memberId, String otpCode) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberDoesNotExistException::new);
        if (!member.isTotpEnabled()) {
            throw new TotpNotEnabledException();
        }

        if (!totpService.verifyMemberCode(member, otpCode)) {
            throw new TotpInvalidCodeException();
        }

        member.disableTotp();
        memberRepository.save(member);
    }
}


