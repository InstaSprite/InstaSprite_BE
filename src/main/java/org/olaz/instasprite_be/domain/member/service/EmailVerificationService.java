package org.olaz.instasprite_be.domain.member.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.entity.EmailVerificationToken;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.entity.MemberProvider;
import org.olaz.instasprite_be.domain.member.exception.EmailVerifyTokenExpiredException;
import org.olaz.instasprite_be.domain.member.exception.EmailVerifyTokenInvalidException;
import org.olaz.instasprite_be.domain.member.repository.EmailVerificationTokenRepository;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.notification.event.EmailVerifiedEvent;
import org.olaz.instasprite_be.global.error.exception.EmailSendFailException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final EmailVerificationTokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${security.email.verify.link-base-url:}")
    private String linkBaseUrl;

    @Value("${security.email.verify.ttl-minutes:30}")
    private long ttlMinutes;

    @Value("${spring.mail.username:no-reply@instasprite}")
    private String from;

    @Transactional
    public void sendVerificationEmailForLocalMember(Member member) {
        if (member.getProvider() != MemberProvider.LOCAL) {
            return;
        }
        if (member.isEmailVerified()) {
            return;
        }
        if (member.getEmail() == null || member.getEmail().isBlank()) {
            return;
        }

        IssueResult issue = issueToken(member.getId());
        String link = buildVerifyLink(issue.getRawToken());

        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(member.getEmail());
            helper.setSubject("Verify your email");

            String html = """
                    <p>Tap to verify your email:</p>
                    <p><a href="%s">Verify email</a></p>
                    <p>If the button doesn’t open, copy and paste this URL:</p>
                    <p>%s</p>
                    <p>This link expires in %d minutes.</p>
                    """.formatted(link, link, ttlMinutes);

            helper.setText(html, true);
            mailSender.send(mime);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}", member.getEmail(), e);
            throw new EmailSendFailException();
        }
    }

    @Transactional
    public IssueResult issueToken(Long memberId) {
        Member member = memberRepository.findByIdForUpdate(memberId).orElseThrow(EmailVerifyTokenInvalidException::new);

        tokenRepository.deleteAllUnusedByMemberId(memberId);

        String raw = generateRawToken();
        String hash = sha256Hex(raw);

        EmailVerificationToken token = new EmailVerificationToken(
                member,
                hash,
                LocalDateTime.now().plus(Duration.ofMinutes(ttlMinutes))
        );
        tokenRepository.save(token);

        return IssueResult.builder()
                .rawToken(raw)
                .expiresAt(token.getExpiresAt())
                .build();
    }

    @Transactional
    public void verifyByToken(String rawToken) {
        String token = rawToken == null ? "" : rawToken.trim();
        if (token.isEmpty()) {
            throw new EmailVerifyTokenInvalidException();
        }

        String hash = sha256Hex(token);
        EmailVerificationToken row = tokenRepository.findByTokenHash(hash)
                .orElseThrow(EmailVerifyTokenInvalidException::new);

        LocalDateTime now = LocalDateTime.now();
        if (row.isUsed()) {
            if (row.getMember().isEmailVerified()) {
                return;
            }
            throw new EmailVerifyTokenInvalidException();
        }
        if (row.isExpired(now)) {
            throw new EmailVerifyTokenExpiredException();
        }

        Member member = row.getMember();
        boolean wasVerified = member.isEmailVerified();
        member.verifyEmail();
        row.markUsed(now);
        if (!wasVerified) {
            applicationEventPublisher.publishEvent(new EmailVerifiedEvent(member.getId()));
        }
        // JPA dirty checking handles saves
    }

    private String buildVerifyLink(String rawToken) {
        String base = linkBaseUrl == null ? "" : linkBaseUrl.trim();
        if (base.isEmpty()) {
            // fallback to relative path (caller should set public base url in prod)
            base = "/api/v1/auth/email/verify";
        }
        String tokenParam = URLEncoder.encode(rawToken, StandardCharsets.UTF_8);
        if (base.contains("?")) {
            return base + "&token=" + tokenParam;
        }
        return base + "?token=" + tokenParam;
    }

    private static String generateRawToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String sha256Hex(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash token", e);
        }
    }

    @Getter
    @Builder
    public static class IssueResult {
        private final String rawToken;
        private final LocalDateTime expiresAt;
    }
}


