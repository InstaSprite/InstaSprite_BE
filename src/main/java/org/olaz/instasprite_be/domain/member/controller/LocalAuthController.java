package org.olaz.instasprite_be.domain.member.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.LOCAL_LOGIN_SUCCESS;
import static org.olaz.instasprite_be.global.result.ResultCode.LOCAL_REGISTER_SUCCESS;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.dto.JwtResponse;
import org.olaz.instasprite_be.domain.member.dto.LocalLoginRequest;
import org.olaz.instasprite_be.domain.member.dto.LocalRegisterRequest;
import org.olaz.instasprite_be.domain.member.service.LocalAuthService;
import org.olaz.instasprite_be.global.result.ResultResponse;
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

@Slf4j
@Tag(name = "Local Authentication API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1)
public class LocalAuthController {

    private final LocalAuthService localAuthService;

    @Operation(summary = "Register with username/email/password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M006 - Local registration successful."),
            @ApiResponse(responseCode = "400", description = "M002 - Username exists.\nM010 - Email exists."),
            @ApiResponse(responseCode = "401", description = "M011 - Invalid credentials.")
    })
    @PostMapping(UrlConstant.AUTH_REGISTER)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody LocalRegisterRequest request) {
        log.info("Local register API called for {}", request.getUsername());

        localAuthService.register(request);

        return ResponseEntity.ok(ResultResponse.of(LOCAL_REGISTER_SUCCESS));
    }

    @Operation(summary = "Login with username/email + password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "M007 - Local login successful."),
            @ApiResponse(responseCode = "400", description = "M012 - Provider mismatch."),
            @ApiResponse(responseCode = "401", description = "M011 - Invalid credentials.")
    })
    @PostMapping(UrlConstant.AUTH_LOGIN)
    public ResponseEntity<ResultResponse> login(@Valid @RequestBody LocalLoginRequest request) {
        log.info("Local login API called for {}", request.getIdentifier());

        JwtDto jwtDto = localAuthService.login(request);

        JwtResponse jwtResponse = JwtResponse.builder()
                .type(jwtDto.getType())
                .accessToken(jwtDto.getAccessToken())
                .refreshToken(jwtDto.getRefreshToken())
                .name(jwtDto.getName())
                .username(jwtDto.getUsername())
                .email(jwtDto.getEmail())
                .isFirstTime(jwtDto.getIsFirstTime())
                .build();

        return ResponseEntity.ok(ResultResponse.of(LOCAL_LOGIN_SUCCESS, jwtResponse));
    }
}

