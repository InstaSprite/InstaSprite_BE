package org.olaz.instasprite_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "JWT token response data model")
@Getter
@Builder
@AllArgsConstructor
public class JwtResponse {

	@Schema(description = "Token type", example = "Bearer")
	private String type;

	@Schema(description = "Access token", example = "AAA.BBB.CCC")
	private String accessToken;

	@Schema(description = "Refresh token", example = "DDD.EEE.FFF")
	private String refreshToken;

	@Schema(description = "User name", example = "John Doe")
	private String name;

	@Schema(description = "User username", example = "johndoe")
	private String username;

	@Schema(description = "User email", example = "john@example.com")
	private String email;

	@Schema(description = "Is first time login", example = "true")
	private Boolean isFirstTime;

}
