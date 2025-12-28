package org.olaz.instasprite_be.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.ForgotPasswordRequest;
import org.olaz.instasprite_be.domain.member.dto.ResetPasswordRequest;
import org.olaz.instasprite_be.domain.member.service.PasswordResetService;
import org.olaz.instasprite_be.global.result.ResultCode;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.olaz.instasprite_be.global.util.UrlConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.olaz.instasprite_be.global.result.ResultCode.*;

@Slf4j
@Tag(name = "Password Reset API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1)
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Operation(summary = "Request password reset")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M038 - Password reset email was sent."),
            @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type."),
            @ApiResponse(responseCode = "500", description = "E001 - Error occurred while sending email.")
    })
    @PostMapping(UrlConstant.AUTH_PASSWORD_FORGOT)
    public ResponseEntity<ResultResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());

        passwordResetService.sendPasswordResetEmail(request.getEmail());

        return ResponseEntity.ok(ResultResponse.of(PASSWORD_RESET_EMAIL_SENT));
    }

    @Operation(summary = "Reset password with temporary password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M039 - Password was reset successfully."),
            @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type.\n"
                    + "M022 - Invalid password reset token.\nM023 - Password reset token has expired.\n"
                    + "M024 - New password cannot be the same as old password."),
    })
    @PostMapping(UrlConstant.AUTH_PASSWORD_RESET)
    public ResponseEntity<ResultResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Password reset attempt");

        passwordResetService.resetPassword(request);

        return ResponseEntity.ok(ResultResponse.of(PASSWORD_RESET_SUCCESS));
    }
}

