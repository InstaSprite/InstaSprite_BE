package org.olaz.instasprite_be.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.global.vo.Image;


@Getter
@NoArgsConstructor
public class MemberDto {

	private Long id;
	private String username;
	private String name;
	private Image image;

	@QueryProjection
	public MemberDto(Member member) {
		this.id = member.getId();
		this.username = member.getUsername();
		this.name = member.getName();
		this.image = member.getImage();
	}


}

