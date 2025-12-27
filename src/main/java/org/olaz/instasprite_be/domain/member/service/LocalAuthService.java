package org.olaz.instasprite_be.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.dto.LocalLoginRequest;
import org.olaz.instasprite_be.domain.member.dto.LocalRegisterRequest;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.entity.MemberProvider;
import org.olaz.instasprite_be.domain.member.exception.EmailAlreadyRegisteredException;
import org.olaz.instasprite_be.domain.member.exception.InvalidCredentialsException;
import org.olaz.instasprite_be.domain.member.exception.ProviderMismatchException;
import org.olaz.instasprite_be.domain.member.exception.TotpInvalidCodeException;
import org.olaz.instasprite_be.domain.member.exception.TotpRequiredException;
import org.olaz.instasprite_be.domain.member.exception.UsernameAlreadyExistException;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.global.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TotpService totpService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void register(LocalRegisterRequest request) {
        log.info("Local registration attempt for username {}", request.getUsername());

        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException();
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider(MemberProvider.LOCAL)
                .build();

        Member saved = memberRepository.save(member);
        emailVerificationService.sendVerificationEmailForLocalMember(saved);
    }

    @Transactional(readOnly = true)
    public JwtDto login(LocalLoginRequest request) {
        log.info("Local login attempt for identifier {}", request.getIdentifier());

        Member member = findByIdentifier(request.getIdentifier())
                .orElseThrow(InvalidCredentialsException::new);

        if (member.getProvider() != MemberProvider.LOCAL) {
            throw new ProviderMismatchException();
        }

        if (member.getPassword() == null || !passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (member.isTotpEnabled()) {
            if (request.getOtpCode() == null || request.getOtpCode().isBlank()) {
                throw new TotpRequiredException();
            }
            if (!totpService.verifyMemberCode(member, request.getOtpCode())) {
                throw new TotpInvalidCodeException();
            }
        }

        JwtDto tokens = jwtUtil.generateTokenDto(member);

        return JwtDto.builder()
                .type(tokens.getType())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .name(tokens.getName())
                .username(tokens.getUsername())
                .email(tokens.getEmail())
                .isFirstTime(false)
                .build();
    }

    private Optional<Member> findByIdentifier(String identifier) {
        Optional<Member> byUsername = memberRepository.findByUsername(identifier);
        if (byUsername.isPresent()) {
            return byUsername;
        }
        return memberRepository.findByEmail(identifier);
    }
}

