package org.olaz.instasprite_be.domain.member.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;

import jakarta.validation.Valid;

import org.olaz.instasprite_be.global.util.UrlConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.member.dto.GoogleLoginRequest;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.dto.JwtResponse;
import org.olaz.instasprite_be.domain.member.service.GoogleOAuthService;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Slf4j
@Tag(name = "Google Authentication API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1)
public class GoogleAuthController {

    private final GoogleOAuthService googleOAuthService;

    @Operation(summary = "Login with Google OAuth")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "G001 - Google login successful."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
            + "G004 - Invalid input type."),
        @ApiResponse(responseCode = "401", description = "G005 - Invalid Google token.")
    })
    @PostMapping(UrlConstant.AUTH_GOOGLE)
    public ResponseEntity<ResultResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        log.info("Google login attempt");

        final JwtDto jwtDto = googleOAuthService.loginWithGoogle(request.getIdToken());

        final JwtResponse jwtResponse = JwtResponse.builder()
                .type(jwtDto.getType())
                .accessToken(jwtDto.getAccessToken())
                .refreshToken(jwtDto.getRefreshToken())
                .name(jwtDto.getName())
                .username(jwtDto.getUsername())
                .email(jwtDto.getEmail())
                .isFirstTime(jwtDto.getIsFirstTime())
                .build();

        return ResponseEntity.ok(ResultResponse.of(GOOGLE_LOGIN_SUCCESS, jwtResponse));
    }

}

