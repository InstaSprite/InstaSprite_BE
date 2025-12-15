package org.olaz.instasprite_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.olaz.instasprite_be.domain.member.entity.Member;

@Schema(description = "User profile edit response model")
@Getter
@Builder
@AllArgsConstructor
public class EditProfileResponse {

	@Schema(description = "Username", example = "dlwlrma")
	private String memberUsername;

	@Schema(description = "Profile image URL", example = "https://drive.google.com/file/d/1Gu0DcGCJNs4Vo0bz2U9U6v01d_VwKijs/view?usp=sharing")
	private String memberImageUrl;

	@Schema(description = "Name", example = "John Doe")
	private String memberName;

	@Schema(description = "Introduction", example = "Hello everyone")
	private String memberIntroduce;

	@Schema(description = "Email", example = "aaa@gmail.com")
	private String memberEmail;

	public EditProfileResponse(Member member) {
		this.memberUsername = member.getUsername();
		this.memberName = member.getName();
		this.memberIntroduce = member.getIntroduce();
		this.memberEmail = member.getEmail();
	}

}

