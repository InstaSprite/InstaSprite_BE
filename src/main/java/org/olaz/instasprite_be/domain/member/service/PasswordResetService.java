package org.olaz.instasprite_be.domain.member.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.ResetPasswordRequest;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.entity.MemberProvider;
import org.olaz.instasprite_be.domain.member.entity.PasswordResetToken;
import org.olaz.instasprite_be.domain.member.exception.PasswordResetTokenExpiredException;
import org.olaz.instasprite_be.domain.member.exception.PasswordResetTokenInvalidException;
import org.olaz.instasprite_be.domain.member.exception.PasswordSameAsOldException;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.member.repository.PasswordResetTokenRepository;
import org.olaz.instasprite_be.global.error.exception.EmailSendFailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int TEMP_PASSWORD_LENGTH = 12;

    private final PasswordResetTokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.password.reset.ttl-minutes:30}")
    private long ttlMinutes;

    @Value("${spring.mail.username:no-reply@instasprite}")
    private String from;

    @Transactional
    public void sendPasswordResetEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        
        // Don't reveal if email exists - return silently if not found
        if (member == null) {
            log.info("Password reset requested for non-existent email: {}", email);
            return;
        }

        // Only allow password reset for LOCAL provider accounts
        if (member.getProvider() != MemberProvider.LOCAL) {
            log.info("Password reset requested for non-LOCAL account: {}", email);
            return;
        }

        if (member.getEmail() == null || member.getEmail().isBlank()) {
            log.warn("Member {} has blank email", member.getId());
            return;
        }

        IssueResult issue = issueToken(member.getId());
        String temporaryPassword = issue.getTemporaryPassword();

        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(member.getEmail());
            helper.setSubject("Password Reset - InstaSprite");

            String html = """
                    <p>You requested a password reset for your InstaSprite account.</p>
                    <p>Your temporary password is: <strong>%s</strong></p>
                    <p>Please use this temporary password to set a new password. This temporary password expires in %d minutes.</p>
                    <p>If you did not request this password reset, please ignore this email.</p>
                    """.formatted(temporaryPassword, ttlMinutes);

            helper.setText(html, true);
            mailSender.send(mime);
            log.info("Password reset email sent to {}", member.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}", member.getEmail(), e);
            throw new EmailSendFailException();
        }
    }

    @Transactional
    public IssueResult issueToken(Long memberId) {
        Member member = memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(PasswordResetTokenInvalidException::new);

        tokenRepository.deleteAllUnusedByMemberId(memberId);

        String temporaryPassword = generateTemporaryPassword();
        String hash = sha256Hex(temporaryPassword);

        PasswordResetToken token = new PasswordResetToken(
                member,
                hash,
                LocalDateTime.now().plus(Duration.ofMinutes(ttlMinutes))
        );
        tokenRepository.save(token);

        return IssueResult.builder()
                .temporaryPassword(temporaryPassword)
                .expiresAt(token.getExpiresAt())
                .build();
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String tempPassword = request.getTemporaryPassword() == null ? "" : request.getTemporaryPassword().trim();
        if (tempPassword.isEmpty()) {
            throw new PasswordResetTokenInvalidException();
        }

        String hash = sha256Hex(tempPassword);
        PasswordResetToken token = tokenRepository.findByTokenHash(hash)
                .orElseThrow(PasswordResetTokenInvalidException::new);

        LocalDateTime now = LocalDateTime.now();
        if (token.isUsed()) {
            throw new PasswordResetTokenInvalidException();
        }
        if (token.isExpired(now)) {
            throw new PasswordResetTokenExpiredException();
        }

        Member member = token.getMember();
        
        // Check if new password is the same as old password
        if (member.getPassword() != null && passwordEncoder.matches(request.getNewPassword(), member.getPassword())) {
            throw new PasswordSameAsOldException();
        }

        // Update password
        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        token.markUsed(now);
        
        log.info("Password reset successful for member: {}", member.getId());
    }

    private String generateTemporaryPassword() {
        StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_CHARS.charAt(SECURE_RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return password.toString();
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
        private final String temporaryPassword;
        private final LocalDateTime expiresAt;
    }
}

