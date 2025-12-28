package org.olaz.instasprite_be.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Register/unregister an FCM device token for the logged-in member")
public record UpsertFcmTokenRequest(
        @Schema(description = "FCM registration token", example = "eYq...:APA91b...")
        @NotBlank
        String token
) {}


