package org.olaz.instasprite_be.global.util.totp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Optional AES-GCM encryption for storing TOTP shared secrets at rest.
 *
 * If {@code security.totp.encryption-key} is not set, secrets are stored as plaintext Base32 (dev-friendly).
 * Provide a 16/24/32-byte AES key as base64 in production.
 */
@Component
public class TotpSecretCrypto {

    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_BITS = 128;
    private static final int IV_BYTES = 12;

    private final SecretKey key; // nullable when encryption disabled
    private final SecureRandom secureRandom = new SecureRandom();

    public TotpSecretCrypto(@Value("${security.totp.encryption-key:}") String keyBase64) {
        if (keyBase64 == null) {
            this.key = null;
            return;
        }

        String normalized = keyBase64.trim();
        if (normalized.isBlank()) {
            this.key = null;
            return;
        }
        // tolerate accidental quotes in .properties defaults / env vars
        if (normalized.length() >= 2 && normalized.startsWith("\"") && normalized.endsWith("\"")) {
            normalized = normalized.substring(1, normalized.length() - 1).trim();
        }

        byte[] raw = Base64.getDecoder().decode(normalized);
        if (!(raw.length == 16 || raw.length == 24 || raw.length == 32)) {
            throw new IllegalArgumentException("security.totp.encryption-key must decode to 16/24/32 bytes (AES-128/192/256)");
        }
        this.key = new SecretKeySpec(raw, "AES");
    }

    public boolean isEncryptionEnabled() {
        return key != null;
    }

    public String encryptForStorage(String secretBase32) {
        if (key == null) {
            return secretBase32;
        }
        try {
            byte[] iv = new byte[IV_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] ciphertext = cipher.doFinal(secretBase32.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt TOTP secret", e);
        }
    }

    public String decryptFromStorage(String stored) {
        if (stored == null) {
            return null;
        }
        if (key == null) {
            return stored;
        }

        try {
            byte[] combined = Base64.getDecoder().decode(stored);
            if (combined.length <= IV_BYTES) {
                // Probably plaintext that got stored by accident; just return it.
                return stored;
            }

            byte[] iv = new byte[IV_BYTES];
            byte[] ciphertext = new byte[combined.length - IV_BYTES];
            System.arraycopy(combined, 0, iv, 0, IV_BYTES);
            System.arraycopy(combined, IV_BYTES, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] plain = cipher.doFinal(ciphertext);
            return new String(plain, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Allow plaintext fallback to avoid bricking accounts if env key changes.
            return stored;
        }
    }
}


