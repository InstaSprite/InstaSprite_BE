package org.olaz.instasprite_be.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtDto {

	private String type;

	private String accessToken;

	private String refreshToken;

	private String name;

	private String username;

	private String email;

	private Boolean isFirstTime;

}
