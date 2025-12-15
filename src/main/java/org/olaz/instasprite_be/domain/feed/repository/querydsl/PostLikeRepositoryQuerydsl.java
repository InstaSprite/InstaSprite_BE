package org.olaz.instasprite_be.domain.feed.repository.querydsl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.olaz.instasprite_be.domain.feed.dto.PostLikeCountDto;
import org.olaz.instasprite_be.domain.feed.dto.PostLikeDto;
import org.olaz.instasprite_be.domain.member.dto.LikeMemberDto;
import org.olaz.instasprite_be.domain.member.entity.Member;

public interface PostLikeRepositoryQuerydsl {

	List<PostLikeDto> findAllPostLikeDtoOfFollowingsByMemberIdAndPostIdIn(Long memberId, List<Long> postIds);

	Page<LikeMemberDto> findPostLikeMembersDtoPageExceptMeByPostIdAndMemberId(Pageable pageable, Long postId, Long memberId);

	Page<LikeMemberDto> findPostLikeMembersDtoPageOfFollowingsByMemberIdAndPostId(Pageable pageable, Long memberId, Long postId);

	Page<LikeMemberDto> findCommentLikeMembersDtoPageExceptMeByCommentIdAndMemberId(Pageable pageable, Long commentId, Long memberId);

	List<PostLikeCountDto> findAllPostLikeCountDtoOfFollowingsLikedPostByMemberAndPostIdIn(Member member,
		List<Long> postIds);

}
