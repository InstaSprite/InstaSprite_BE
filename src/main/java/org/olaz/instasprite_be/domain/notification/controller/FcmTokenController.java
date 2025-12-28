package org.olaz.instasprite_be.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olaz.instasprite_be.domain.notification.dto.UpsertFcmTokenRequest;
import org.olaz.instasprite_be.domain.notification.service.FcmTokenService;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.olaz.instasprite_be.global.util.AuthUtil;
import org.olaz.instasprite_be.global.util.UrlConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.olaz.instasprite_be.global.result.ResultCode.FCM_TOKEN_DELETE_SUCCESS;
import static org.olaz.instasprite_be.global.result.ResultCode.FCM_TOKEN_UPSERT_SUCCESS;

@Tag(name = "FCM Token API")
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1 + UrlConstant.NOTIFICATIONS)
public class FcmTokenController {

    private final AuthUtil authUtil;
    private final FcmTokenService fcmTokenService;

    @Operation(summary = "Register (or refresh) a device's FCM token for the logged-in user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token saved."),
            @ApiResponse(responseCode = "401", description = "Login required.")
    })
    @PostMapping("/fcm-token")
    public ResponseEntity<ResultResponse> upsert(@Valid @RequestBody UpsertFcmTokenRequest request) {
        fcmTokenService.upsert(authUtil.getLoginMemberId(), request.token());
        return ResponseEntity.ok(ResultResponse.of(FCM_TOKEN_UPSERT_SUCCESS));
    }

    @Operation(summary = "Unregister a device's FCM token for the logged-in user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token deleted."),
            @ApiResponse(responseCode = "401", description = "Login required.")
    })
    @DeleteMapping("/fcm-token")
    public ResponseEntity<ResultResponse> delete(@Valid @RequestBody UpsertFcmTokenRequest request) {
        fcmTokenService.delete(authUtil.getLoginMemberId(), request.token());
        return ResponseEntity.ok(ResultResponse.of(FCM_TOKEN_DELETE_SUCCESS));
    }
}


