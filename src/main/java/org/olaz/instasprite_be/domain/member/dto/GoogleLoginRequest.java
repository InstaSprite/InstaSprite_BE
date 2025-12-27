package org.olaz.instasprite_be.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "Google OAuth login request")
public class GoogleLoginRequest {

    @Schema(description = "Google ID token from Android app", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU...")
    @NotBlank(message = "Google ID token is required")
    private String idToken;

    @Schema(description = "6-digit OTP from authenticator app (required if 2FA is enabled)", example = "123456")
    private String otpCode;

}

