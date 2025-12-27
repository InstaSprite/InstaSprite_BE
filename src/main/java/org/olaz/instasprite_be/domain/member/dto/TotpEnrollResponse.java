package org.olaz.instasprite_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "TOTP enrollment response (scan otpauthUri or enter secret manually)")
public class TotpEnrollResponse {

    @Schema(description = "Whether 2FA is enabled already", example = "false")
    private final boolean enabled;

    @Schema(description = "Issuer shown in the authenticator app", example = "InstaSprite")
    private final String issuer;

    @Schema(description = "Account label (usually username/email)", example = "pixelartist")
    private final String accountName;

    @Schema(description = "Base32 secret for manual entry (show once)", example = "JBSWY3DPEHPK3PXP")
    private final String secret;

    @Schema(description = "otpauth:// URI (frontend should render QR from this)", example = "otpauth://totp/InstaSprite:pixelartist?secret=...&issuer=InstaSprite&algorithm=SHA1&digits=6&period=30")
    private final String otpauthUri;

    @Schema(description = "QR code PNG (Base64) for saving/displaying", example = "iVBORw0KGgoAAAANSUhEUgAA...")
    private final String qrCodePngBase64;

    @Schema(description = "Whether secret was encrypted at rest on the server", example = "true")
    private final boolean encryptionEnabled;
}


