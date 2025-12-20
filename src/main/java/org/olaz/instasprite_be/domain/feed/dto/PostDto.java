package org.olaz.instasprite_be.domain.feed.dto;

import static org.olaz.instasprite_be.global.util.ConstantUtils.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

	private Long postId;
	private String postContent;
	@Setter
    private List<String> mentionsOfContent = new ArrayList<>();
	@Setter
    private List<String> hashtagsOfContent = new ArrayList<>();
	@Setter
    private List<PostImageDto> postImages = new ArrayList<>();
	private LocalDateTime postUploadDate;
	private MemberDto member;
	private long postCommentsCount;
	@Setter
    private long postLikesCount;
	private boolean postBookmarkFlag;
	private boolean postLikeFlag;
	private boolean commentOptionFlag;
	private boolean isFollowing;
	@Setter
    private String followingMemberUsernameLikedPost = EMPTY;
	@Setter
    private List<CommentDto> recentComments = new ArrayList<>();

	@QueryProjection
	public PostDto(Long postId, String postContent, LocalDateTime postUploadDate, Member member, int postCommentsCount,
		int postLikesCount, boolean postBookmarkFlag, boolean postLikeFlag, boolean commentOptionFlag,
		 boolean isFollowing) {
		this.postId = postId;
		this.postContent = postContent;
		this.postUploadDate = postUploadDate;
		this.member = new MemberDto(member);
		this.postCommentsCount = postCommentsCount;
		this.postLikesCount = postLikesCount;
		this.postBookmarkFlag = postBookmarkFlag;
		this.postLikeFlag = postLikeFlag;
		this.commentOptionFlag = commentOptionFlag;
		this.isFollowing = isFollowing;
	}

	@QueryProjection
	public PostDto(Long postId, String postContent, LocalDateTime postUploadDate, Member member, int postCommentsCount,
		int postLikesCount, boolean commentOptionFlag) {
		this.postId = postId;
		this.postContent = postContent;
		this.postUploadDate = postUploadDate;
		this.member = new MemberDto(member);
		this.postCommentsCount = postCommentsCount;
		this.postLikesCount = postLikesCount;
		this.postBookmarkFlag = false;
		this.postLikeFlag = false;
		this.commentOptionFlag = commentOptionFlag;
		this.isFollowing = false;
	}

}
