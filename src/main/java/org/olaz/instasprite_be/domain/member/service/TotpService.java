package org.olaz.instasprite_be.domain.member.service;

import lombok.Builder;
import lombok.Getter;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.global.util.totp.Base32;
import org.olaz.instasprite_be.global.util.totp.TotpSecretCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;

@Service
public class TotpService {

    private static final int SECRET_BYTES = 20; // 160-bit (common for TOTP)
    private static final int DIGITS = 6;
    private static final int PERIOD_SECONDS = 30;
    private static final int DEFAULT_WINDOW = 1; // accept +/- 1 time-step

    private final SecureRandom secureRandom = new SecureRandom();
    private final TotpSecretCrypto totpSecretCrypto;
    private final String issuer;

    public TotpService(TotpSecretCrypto totpSecretCrypto, @Value("${security.totp.issuer:InstaSprite}") String issuer) {
        this.totpSecretCrypto = totpSecretCrypto;
        this.issuer = issuer;
    }

    public Enrollment enroll(Member member) {
        byte[] secretBytes = new byte[SECRET_BYTES];
        secureRandom.nextBytes(secretBytes);
        String secretBase32 = Base32.encode(secretBytes);

        String otpauthUri = buildOtpAuthUri(issuer, member.getUsername(), secretBase32);
        String stored = totpSecretCrypto.encryptForStorage(secretBase32);

        member.setupTotpSecret(stored);

        return Enrollment.builder()
                .issuer(issuer)
                .accountName(member.getUsername())
                .secret(secretBase32)
                .otpauthUri(otpauthUri)
                .encryptionEnabled(totpSecretCrypto.isEncryptionEnabled())
                .build();
    }

    public boolean verifyMemberCode(Member member, String otpCode) {
        if (member.getTotpSecret() == null) {
            return false;
        }
        String secretBase32 = totpSecretCrypto.decryptFromStorage(member.getTotpSecret());
        return verifyCode(secretBase32, otpCode, Instant.now().getEpochSecond(), DEFAULT_WINDOW);
    }

    public static boolean verifyCode(String secretBase32, String otpCode, long nowEpochSeconds, int window) {
        if (secretBase32 == null || secretBase32.isBlank()) {
            return false;
        }
        if (otpCode == null) {
            return false;
        }

        String code = otpCode.trim();
        if (!code.matches("^\\d{" + DIGITS + "}$")) {
            return false;
        }

        long timeStep = nowEpochSeconds / PERIOD_SECONDS;
        for (int i = -window; i <= window; i++) {
            String expected = generateTotp(secretBase32, timeStep + i);
            if (code.equals(expected)) {
                return true;
            }
        }

        return false;
    }

    private static String generateTotp(String secretBase32, long timeStep) {
        try {
            byte[] key = Base32.decode(secretBase32);
            byte[] counter = ByteBuffer.allocate(8).putLong(timeStep).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hmac = mac.doFinal(counter);

            int offset = hmac[hmac.length - 1] & 0x0f;
            int binary =
                    ((hmac[offset] & 0x7f) << 24) |
                    ((hmac[offset + 1] & 0xff) << 16) |
                    ((hmac[offset + 2] & 0xff) << 8) |
                    (hmac[offset + 3] & 0xff);

            int otp = binary % (int) Math.pow(10, DIGITS);
            return String.format("%0" + DIGITS + "d", otp);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate TOTP", e);
        }
    }

    private static String buildOtpAuthUri(String issuer, String accountName, String secretBase32) {
        String label = issuer + ":" + accountName;
        String labelEnc = urlEncode(label);
        String issuerEnc = urlEncode(issuer);
        String secretEnc = urlEncode(secretBase32);

        return "otpauth://totp/" + labelEnc +
                "?secret=" + secretEnc +
                "&issuer=" + issuerEnc +
                "&algorithm=SHA1&digits=" + DIGITS + "&period=" + PERIOD_SECONDS;
    }

    private static String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    @Getter
    @Builder
    public static class Enrollment {
        private final String issuer;
        private final String accountName;
        private final String secret;
        private final String otpauthUri;
        private final boolean encryptionEnabled;
    }
}


