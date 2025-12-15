package org.olaz.instasprite_be.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.dto.JwtResponse;
import org.olaz.instasprite_be.domain.member.dto.RefreshTokenRequest;
import org.olaz.instasprite_be.domain.member.service.RefreshTokenService;
import org.olaz.instasprite_be.global.result.ResultCode;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.olaz.instasprite_be.global.util.UrlConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.olaz.instasprite_be.global.result.ResultCode.TOKEN_REFRESH_SUCCESS;

@Slf4j
@Tag(name = "Token Refresh API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1)
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Refresh JWT tokens")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "T001 - Token refresh successful."),
        @ApiResponse(responseCode = "400", description = "J001 - Invalid token.\n" + "J003 - Expired refresh token."),
        @ApiResponse(responseCode = "401", description = "J002 - Expired token.")
    })
    @PostMapping(UrlConstant.AUTH_REFRESH)
    public ResponseEntity<ResultResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request");

        final JwtDto jwtDto = refreshTokenService.refreshToken(request.getRefreshToken());

        final JwtResponse jwtResponse = JwtResponse.builder()
                .type(jwtDto.getType())
                .accessToken(jwtDto.getAccessToken())
                .refreshToken(jwtDto.getRefreshToken())
                .build();

        return ResponseEntity.ok(ResultResponse.of(TOKEN_REFRESH_SUCCESS, jwtResponse));
    }
}
