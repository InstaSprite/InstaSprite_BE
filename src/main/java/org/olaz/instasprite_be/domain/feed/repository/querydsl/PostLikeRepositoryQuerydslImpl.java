package org.olaz.instasprite_be.domain.feed.repository.querydsl;

import static org.olaz.instasprite_be.domain.feed.entity.QCommentLike.*;
import static org.olaz.instasprite_be.domain.feed.entity.QPostLike.*;
import static org.olaz.instasprite_be.domain.follow.entity.QFollow.*;
import static org.olaz.instasprite_be.domain.member.entity.QMember.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.PostLikeCountDto;
import org.olaz.instasprite_be.domain.feed.dto.PostLikeDto;
import org.olaz.instasprite_be.domain.feed.dto.QPostLikeCountDto;
import org.olaz.instasprite_be.domain.feed.dto.QPostLikeDto;
import org.olaz.instasprite_be.domain.member.dto.LikeMemberDto;
import org.olaz.instasprite_be.domain.member.dto.QLikeMemberDto;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.entity.QMember;

@RequiredArgsConstructor
public class PostLikeRepositoryQuerydslImpl implements PostLikeRepositoryQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<PostLikeDto> findAllPostLikeDtoOfFollowingsByMemberIdAndPostIdIn(Long memberId, List<Long> postIds) {
		return queryFactory
			.select(new QPostLikeDto(
				postLike.post.id,
				postLike.member.username
			))
			.from(postLike)
			.innerJoin(postLike.member, member)
			.where(postLike.post.id.in(postIds).and(postLike.member.in(getFollowingMembersByMemberId(memberId))))
			.fetch();
	}

	@Override
	public Page<LikeMemberDto> findPostLikeMembersDtoPageExceptMeByPostIdAndMemberId(Pageable pageable, Long postId, Long memberId) {
		final List<LikeMemberDto> likeMembersDtos = queryFactory
			.select(new QLikeMemberDto(
				postLike.member,
				isFollowing(memberId, postLike.member),
				isFollower(memberId, postLike.member)
			))
			.from(postLike)
			.innerJoin(postLike.member, member)
			.where(postLike.post.id.eq(postId).and(postLike.member.id.ne(memberId)))
			.orderBy(postLike.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final long total = queryFactory
			.selectFrom(postLike)
			.where(postLike.post.id.eq(postId).and(postLike.member.id.ne(memberId)))
			.fetchCount();

		return new PageImpl<>(likeMembersDtos, pageable, total);
	}

	@Override
	public Page<LikeMemberDto> findPostLikeMembersDtoPageOfFollowingsByMemberIdAndPostId(Pageable pageable,
		Long memberId, Long postId) {
		final List<LikeMemberDto> likeMembersDtos = queryFactory
			.select(new QLikeMemberDto(
				postLike.member,
				isFollowing(memberId, postLike.member),
				isFollower(memberId, postLike.member)
			))
			.from(postLike)
			.innerJoin(postLike.member, member)
			.where(postLike.post.id.eq(postId).and(postLike.member.in(getFollowingMembersByMemberId(memberId))))
			.orderBy(postLike.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final long total = queryFactory
			.selectFrom(postLike)
			.where(postLike.post.id.eq(postId).and(postLike.member.in(getFollowingMembersByMemberId(memberId))))
			.fetchCount();

		return new PageImpl<>(likeMembersDtos, pageable, total);
	}

	@Override
	public Page<LikeMemberDto> findCommentLikeMembersDtoPageExceptMeByCommentIdAndMemberId(Pageable pageable, Long commentId, Long memberId) {
		final List<LikeMemberDto> likeMembersDtos = queryFactory
			.select(new QLikeMemberDto(
				commentLike.member,
				isFollowing(memberId, commentLike.member),
				isFollower(memberId, commentLike.member)
			))
			.from(commentLike)
			.innerJoin(commentLike.member, member)
			.where(commentLike.comment.id.eq(commentId).and(commentLike.member.id.ne(memberId)))
			.orderBy(commentLike.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final long total = queryFactory
			.selectFrom(commentLike)
			.where(commentLike.comment.id.eq(commentId).and(commentLike.member.id.ne(memberId)))
			.fetchCount();

		return new PageImpl<>(likeMembersDtos, pageable, total);
	}

	@Override
	public List<PostLikeCountDto> findAllPostLikeCountDtoOfFollowingsLikedPostByMemberAndPostIdIn(Member member,
		List<Long> postIds) {
		return queryFactory
			.select(new QPostLikeCountDto(
				postLike.post.id,
				postLike.count()
			))
			.from(postLike)
			.join(follow).on(follow.followMember.eq(postLike.member).and(follow.member.eq(member)))
			.where(postLike.post.id.in(postIds))
			.groupBy(postLike.post.id)
			.fetch();
	}

	private JPQLQuery<Member> getFollowingMembersByMemberId(Long memberId) {
		return JPAExpressions
			.select(follow.followMember)
			.from(follow)
			.innerJoin(follow.followMember, member)
			.where(follow.member.id.eq(memberId));
	}

	private BooleanExpression isFollower(Long memberId, QMember member) {
		return JPAExpressions
			.selectFrom(follow)
			.where(follow.member.eq(member).and(follow.followMember.id.eq(memberId)))
			.exists();
	}

	private BooleanExpression isFollowing(Long memberId, QMember member) {
		return JPAExpressions
			.selectFrom(follow)
			.where(follow.member.id.eq(memberId).and(follow.followMember.eq(member)))
			.exists();
	}

}
