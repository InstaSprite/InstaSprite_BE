package org.olaz.instasprite_be.domain.feed.repository.querydsl;

import static org.olaz.instasprite_be.domain.feed.entity.QBookmark.*;
import static org.olaz.instasprite_be.domain.feed.entity.QPost.*;
import static org.olaz.instasprite_be.domain.feed.entity.QPostLike.*;
import static org.olaz.instasprite_be.domain.follow.entity.QFollow.*;
//import static org.olaz.instasprite_be.domain.hashtag.entity.QHashtagPost.*;
import static org.olaz.instasprite_be.domain.member.entity.QMember.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.PostDto;
import org.olaz.instasprite_be.domain.feed.dto.QPostDto;

@RequiredArgsConstructor
public class PostRepositoryQuerydslImpl implements PostRepositoryQuerydsl {

	private static final Logger log = Logger.getLogger(PostRepositoryQuerydslImpl.class.getName());

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<PostDto> findPostDtoPageOfFollowingMembersOrHashtagsByMemberId(Long memberId, Pageable pageable) {
		final List<PostDto> postDtos = queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				isExistBookmarkWherePostEqMemberIdEq(memberId),
				isExistPostLikeWherePostEqAndMemberIdEq(memberId),
				post.commentFlag,
				isFollowing(memberId)
			))
			.from(post)
			.innerJoin(post.member, member)
			.where(post.member.id.in(getFollowingMemberIdsByMemberId(memberId)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.id.desc())
			.distinct()
			.fetch();

		final long total = queryFactory
			.selectFrom(post)
			.innerJoin(post.member, member)
			.where(post.member.id.in(getFollowingMemberIdsByMemberId(memberId)))
			.fetch()
			.size();

		return new PageImpl<>(postDtos, pageable, total);
	}

	@Override
	public Optional<PostDto> findPostDtoByPostIdAndMemberId(Long postId, Long memberId) {
		return Optional.ofNullable(queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				isExistBookmarkWherePostEqMemberIdEq(memberId),
				isExistPostLikeWherePostEqAndMemberIdEq(memberId),
				post.commentFlag,
				isFollowing(memberId)
			))
			.from(post)
			.where(post.id.eq(postId))
			.fetchOne());
	}

	@Override
	public Optional<PostDto> findPostDtoWithoutLoginByPostId(Long postId) {
		return Optional.ofNullable(queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				post.commentFlag
			))
			.from(post)
			.where(post.id.eq(postId))
			.fetchOne());
	}

	@Override
	public Page<PostDto> findPostDtoPageByMemberIdAndPostIdIn(Pageable pageable, Long memberId, List<Long> postIds) {
		final List<PostDto> postDtos = queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				isExistBookmarkWherePostEqMemberIdEq(memberId),
				isExistPostLikeWherePostEqAndMemberIdEq(memberId),
				post.commentFlag,
//				post.likeFlag,
				isFollowing(memberId)
			))
			.from(post)
			.innerJoin(post.member, member)
			.where(post.id.in(postIds))
			.orderBy(post.id.desc())
			.fetch();

		final long total = queryFactory
			.selectFrom(post)
			.where(post.id.in(postIds))
			.fetchCount();

		return new PageImpl<>(postDtos, pageable, total);
	}

	@Override
	public Page<PostDto> findAllPostDtoPage(Pageable pageable, Long memberId) {
		final List<PostDto> postDtos = queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				isExistBookmarkWherePostEqMemberIdEq(memberId),
				isExistPostLikeWherePostEqAndMemberIdEq(memberId),
				post.commentFlag,
				isFollowing(memberId)
			))
			.from(post)
			.innerJoin(post.member, member)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.id.desc())
			.fetch();

		final long total = queryFactory
			.selectFrom(post)
			.fetchCount();

		return new PageImpl<>(postDtos, pageable, total);
	}

	@Override
	public Page<PostDto> searchPostDtoByContentContains(String content, Pageable pageable, Long memberId) {
		final BooleanExpression predicate = post.content.containsIgnoreCase(content);

		final List<PostDto> postDtos = queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				isExistBookmarkWherePostEqMemberIdEq(memberId),
				isExistPostLikeWherePostEqAndMemberIdEq(memberId),
				post.commentFlag,
				isFollowing(memberId)
			))
			.from(post)
			.innerJoin(post.member, member)
			.where(predicate)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(post.id.desc())
			.fetch();

		final long total = queryFactory
			.selectFrom(post)
			.where(predicate)
			.fetchCount();

		return new PageImpl<>(postDtos, pageable, total);
	}

	private BooleanExpression isFollowing(Long memberId) {
		return JPAExpressions
			.selectFrom(follow)
			.where(follow.member.id.eq(memberId).and(follow.followMember.id.eq(post.member.id)))
			.exists();
	}

	private BooleanExpression isExistPostLikeWherePostEqAndMemberIdEq(Long id) {
		log.fine(() -> "Building postLike exists predicate for memberId=" + id);

		return JPAExpressions
			.selectFrom(postLike)
			.where(postLike.post.eq(post).and(postLike.member.id.eq(id)))
			.exists();
	}

	private BooleanExpression isExistBookmarkWherePostEqMemberIdEq(Long id) {
		return JPAExpressions
			.selectFrom(bookmark)
			.where(bookmark.post.eq(post).and(bookmark.member.id.eq(id)))
			.exists();
	}

	private JPQLQuery<Long> getFollowingMemberIdsByMemberId(Long memberId) {
		return JPAExpressions
			.select(follow.followMember.id)
			.from(follow)
			.where(follow.member.id.eq(memberId));
	}

//	private JPQLQuery<Long> getPostIdsOfFollowingHashtagByMemberId(Long memberId) {
//		return JPAExpressions
//			.select(hashtagPost.post.id)
//			.from(hashtagPost)
//			.join(hashtagFollow).on(hashtagFollow.member.id.eq(memberId)
//				.and(hashtagFollow.hashtag.id.eq(hashtagPost.hashtag.id)))
//			.innerJoin(hashtagPost.post, post).on(hashtagPost.post.member.id.ne(memberId));
//	}

	@Override
	public Optional<PostDto> findMostLikedPostDto() {
		final PostDto postDto = queryFactory
			.select(new QPostDto(
				post.id,
				post.content,
				post.uploadDate,
				post.member,
				post.comments.size(),
				post.postLikes.size(),
				Expressions.constant(false), // postBookmarkFlag - not needed for widget
				Expressions.constant(false), // postLikeFlag - not needed for widget
				post.commentFlag,
//				post.likeFlag,
				Expressions.constant(false) // isFollowing - not needed for widget
			))
			.from(post)
			.innerJoin(post.member, member)
			.orderBy(post.postLikes.size().desc(), post.id.desc())
			.limit(1)
			.fetchFirst();

		return Optional.ofNullable(postDto);
	}

}
