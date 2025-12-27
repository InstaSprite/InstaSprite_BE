package org.olaz.instasprite_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "TOTP 2FA status")
public class TotpStatusResponse {

    @Schema(description = "Whether TOTP 2FA is enabled", example = "true")
    private final boolean enabled;
}


