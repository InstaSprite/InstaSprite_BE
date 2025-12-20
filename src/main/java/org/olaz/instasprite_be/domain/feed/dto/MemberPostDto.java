package org.olaz.instasprite_be.domain.feed.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;

import lombok.*;

import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPostDto {

	private Long postId;

	@JsonIgnore
	private MemberDto member;
	@Setter
    private PostImageDto postImage;
	private boolean hasManyPostImages;

//	@JsonIgnore
//	private boolean likeOptionFlag;

	@JsonIgnore
	private boolean postLikeFlag;
	private int postCommentsCount;
	@Setter
    private int postLikesCount;

	@Builder
	@QueryProjection
	public MemberPostDto(Long postId, Member member, boolean hasManyPostImages, boolean postLikeFlag,
		int postCommentsCount, int postLikesCount) {
		this.postId = postId;
		this.member = new MemberDto(member);
		this.hasManyPostImages = hasManyPostImages;
//		this.likeOptionFlag = likeOptionFlag;
		this.postLikeFlag = postLikeFlag;
		this.postCommentsCount = postCommentsCount;
		this.postLikesCount = postLikesCount;
	}

	@Builder
	@QueryProjection
	public MemberPostDto(Long postId, Member member, boolean hasManyPostImages, int postCommentsCount, int postLikesCount) {
		this.postId = postId;
		this.member = new MemberDto(member);
		this.hasManyPostImages = hasManyPostImages;
		this.postCommentsCount = postCommentsCount;
		this.postLikesCount = postLikesCount;
	}

}
