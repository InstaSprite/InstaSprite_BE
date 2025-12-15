package org.olaz.instasprite_be.domain.follow.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.entity.Member;

@Getter
public class FollowerDto {

	private MemberDto member;
	private boolean isFollowing;
	private boolean isFollower;
	private boolean isMe;

	@QueryProjection
	public FollowerDto(Member member, boolean isFollowing, boolean isFollower, boolean isMe) {
		this.member = new MemberDto(member);
		this.isFollowing = isFollowing;
		this.isFollower = isFollower;
		this.isMe = isMe;
	}

}
