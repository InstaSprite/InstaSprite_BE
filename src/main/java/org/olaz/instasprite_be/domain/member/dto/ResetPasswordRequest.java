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
@Schema(description = "Reset password request")
public class ResetPasswordRequest {

    @Schema(description = "Temporary password received via email", example = "TempPass123!")
    @NotBlank(message = "Temporary password is required")
    private String temporaryPassword;

    @Schema(description = "New password", example = "NewSecureP@ssw0rd", minLength = 8)
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "Password must be 8 to 64 characters")
    private String newPassword;
}

