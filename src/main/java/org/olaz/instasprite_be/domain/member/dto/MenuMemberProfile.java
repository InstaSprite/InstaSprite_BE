package org.olaz.instasprite_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.olaz.instasprite_be.domain.member.entity.Member;

@Schema(description = "Top menu user profile model")
@Getter
@Builder
@AllArgsConstructor
public class MenuMemberProfile {

	@Schema(description = "User ID (Primary Key)", example = "1")
	private Long memberId;

	@Schema(description = "Username", example = "dlwlrma")
	private String memberUsername;

	@Schema(description = "Profile image URL", example = "https://drive.google.com/file/d/1Gu0DcGCJNs4Vo0bz2U9U6v01d_VwKijs/view?usp=sharing")
	private String memberImageUrl;

	@Schema(description = "Name", example = "John Doe")
	private String memberName;

	public MenuMemberProfile(Member member) {
		this.memberId = member.getId();
		this.memberUsername = member.getUsername();
		this.memberImageUrl = member.getImage().getImageUrl();
		this.memberName = member.getName();
	}

}

