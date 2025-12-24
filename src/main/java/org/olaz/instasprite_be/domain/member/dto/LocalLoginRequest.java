package org.olaz.instasprite_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "Local login request using username or email")
public class LocalLoginRequest {

    @Schema(description = "Username or email", example = "pixelartist or user@example.com")
    @NotBlank(message = "Username or email is required")
    @Size(max = 255, message = "Identifier must be at most 255 characters")
    private String identifier;

    @Schema(description = "Password", example = "S3cureP@ssw0rd", minLength = 8)
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be 8 to 64 characters")
    private String password;
}

