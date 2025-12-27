package org.olaz.instasprite_be.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.TotpCodeRequest;
import org.olaz.instasprite_be.domain.member.dto.TotpEnrollResponse;
import org.olaz.instasprite_be.domain.member.dto.TotpStatusResponse;
import org.olaz.instasprite_be.domain.member.service.MemberTwoFactorService;
import org.olaz.instasprite_be.domain.member.service.TotpService;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.olaz.instasprite_be.global.util.AuthUtil;
import org.olaz.instasprite_be.global.util.UrlConstant;
import org.olaz.instasprite_be.global.util.ratelimit.InMemoryRateLimiter;
import org.olaz.instasprite_be.global.util.ratelimit.RateLimitPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

import static org.olaz.instasprite_be.global.result.ResultCode.*;

@Slf4j
@Tag(name = "2FA (TOTP) API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1)
public class TwoFactorController {

    private final AuthUtil authUtil;
    private final MemberTwoFactorService memberTwoFactorService;
    private final InMemoryRateLimiter rateLimiter;

    @Operation(summary = "Get TOTP 2FA status for current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TOTP status retrieved.")
    })
    @GetMapping(UrlConstant.AUTH_2FA_STATUS)
    public ResponseEntity<ResultResponse> status() {
        Long memberId = authUtil.getLoginMemberId();
        boolean enabled = memberTwoFactorService.isTotpEnabled(memberId);
        return ResponseEntity.ok(ResultResponse.of(TOTP_STATUS_SUCCESS, TotpStatusResponse.builder().enabled(enabled).build()));
    }

    @Operation(summary = "Enroll TOTP (returns secret + otpauth URI)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TOTP enrollment generated."),
            @ApiResponse(responseCode = "400", description = "TOTP already enabled.")
    })
    @PostMapping(UrlConstant.AUTH_2FA_ENROLL)
    public ResponseEntity<ResultResponse> enroll() {
        Long memberId = authUtil.getLoginMemberId();
        TotpService.Enrollment enrollment = memberTwoFactorService.enrollTotp(memberId);

        byte[] qrPng = org.olaz.instasprite_be.global.util.totp.QrCodeUtil.toPng(enrollment.getOtpauthUri(), 256);
        String qrB64 = Base64.getEncoder().encodeToString(qrPng);

        TotpEnrollResponse data = TotpEnrollResponse.builder()
                .enabled(false)
                .issuer(enrollment.getIssuer())
                .accountName(enrollment.getAccountName())
                .secret(enrollment.getSecret())
                .otpauthUri(enrollment.getOtpauthUri())
                .qrCodePngBase64(qrB64)
                .encryptionEnabled(enrollment.isEncryptionEnabled())
                .build();

        return ResponseEntity.ok(ResultResponse.of(TOTP_ENROLL_SUCCESS, data));
    }

    @Operation(summary = "Enable TOTP (verify code)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TOTP enabled."),
            @ApiResponse(responseCode = "400", description = "Not enrolled / already enabled."),
            @ApiResponse(responseCode = "401", description = "Invalid OTP.")
    })
    @PostMapping(UrlConstant.AUTH_2FA_ENABLE)
    public ResponseEntity<ResultResponse> enable(@Valid @RequestBody TotpCodeRequest request) {
        Long memberId = authUtil.getLoginMemberId();
        rateLimiter.consumeOrThrow(
                "auth:totp-enable:" + memberId,
                RateLimitPolicy.OTP_CAPACITY,
                RateLimitPolicy.OTP_REFILL,
                1
        );
        memberTwoFactorService.enableTotp(memberId, request.getOtpCode());
        return ResponseEntity.ok(ResultResponse.of(TOTP_ENABLE_SUCCESS));
    }

    @Operation(summary = "Disable TOTP (verify code)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TOTP disabled."),
            @ApiResponse(responseCode = "400", description = "Not enabled."),
            @ApiResponse(responseCode = "401", description = "Invalid OTP.")
    })
    @PostMapping(UrlConstant.AUTH_2FA_DISABLE)
    public ResponseEntity<ResultResponse> disable(@Valid @RequestBody TotpCodeRequest request) {
        Long memberId = authUtil.getLoginMemberId();
        rateLimiter.consumeOrThrow(
                "auth:totp-disable:" + memberId,
                RateLimitPolicy.OTP_CAPACITY,
                RateLimitPolicy.OTP_REFILL,
                1
        );
        memberTwoFactorService.disableTotp(memberId, request.getOtpCode());
        return ResponseEntity.ok(ResultResponse.of(TOTP_DISABLE_SUCCESS));
    }
}


