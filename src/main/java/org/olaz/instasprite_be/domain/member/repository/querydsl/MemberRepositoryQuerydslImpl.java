package org.olaz.instasprite_be.domain.member.repository.querydsl;

import static org.olaz.instasprite_be.domain.feed.entity.QPost.*;
import static org.olaz.instasprite_be.domain.follow.entity.QFollow.*;
//import static org.olaz.instasprite_be.domain.member.entity.QBlock.*;
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

import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.dto.MiniProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.QMemberDto;
import org.olaz.instasprite_be.domain.member.dto.QMiniProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.QUserProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.UserProfileResponse;

@RequiredArgsConstructor
public class MemberRepositoryQuerydslImpl implements MemberRepositoryQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public UserProfileResponse findUserProfileByLoginMemberIdAndTargetUsername(Long loginMemberId, String username) {
		return queryFactory
			.select(new QUserProfileResponse(
				member.username,
				member.name,
				member.image,
				isFollowing(loginMemberId, username),
				isFollower(loginMemberId, username),
//				isBlocking(loginMemberId, username),
//				isBlocked(loginMemberId, username),
				member.introduce,
				getPostCount(username),
				getFollowingCount(username),
				getFollowerCount(username),
				member.id.eq(loginMemberId),
				member.emailVerified))
			.from(member)
			.where(member.username.eq(username))
			.fetchOne();
	}

	@Override
	public MiniProfileResponse findMiniProfileByLoginMemberIdAndTargetUsername(Long loginMemberId, String username) {
		return queryFactory
			.select(new QMiniProfileResponse(
				member.username,
				member.name,
				member.image,
				isFollowing(loginMemberId, username),
				isFollower(loginMemberId, username),
//				isBlocking(loginMemberId, username),
//				isBlocked(loginMemberId, username),
				getPostCount(username),
				getFollowingCount(username),
				getFollowerCount(username),
				member.id.eq(loginMemberId)))
			.from(member)
			.where(member.username.eq(username))
			.fetchOne();
	}

	@Override
	public Page<MemberDto> searchMemberDtoByUsernameContains(String username, Pageable pageable) {
		final BooleanExpression predicate = member.username.containsIgnoreCase(username);

		final List<MemberDto> members = queryFactory
			.select(new QMemberDto(member))
			.from(member)
			.where(predicate)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(member.id.desc())
			.fetch();

		final long total = queryFactory
			.selectFrom(member)
			.where(predicate)
			.fetchCount();

		return new PageImpl<>(members, pageable, total);
	}

	private JPQLQuery<Long> getPostCount(String targetUsername) {
		return JPAExpressions
			.select(post.count())
			.from(post)
			.where(post.member.username.eq(targetUsername));
	}

	private JPQLQuery<Long> getFollowingCount(String targetUsername) {
		return JPAExpressions
			.select(follow.count())
			.from(follow)
			.where(follow.member.username.eq(targetUsername));
	}

	private JPQLQuery<Long> getFollowerCount(String targetUsername) {
		return JPAExpressions
			.select(follow.count())
			.from(follow)
			.where(follow.followMember.username.eq(targetUsername));
	}

	private BooleanExpression isFollowing(Long loginUserId, String targetUsername) {
		return JPAExpressions
			.selectFrom(follow)
			.where(follow.member.id.eq(loginUserId)
				.and(follow.followMember.username.eq(targetUsername)))
			.exists();
	}

	private BooleanExpression isFollower(Long loginUserId, String targetUsername) {
		return JPAExpressions
			.selectFrom(follow)
			.where(follow.member.username.eq(targetUsername)
				.and(follow.followMember.id.eq(loginUserId)))
			.exists();
	}

//	private BooleanExpression isBlocking(Long loginUserId, String targetUsername) {
//		return JPAExpressions
//			.selectFrom(block)
//			.where(block.member.id.eq(loginUserId)
//				.and(block.blockMember.username.eq(targetUsername)))
//			.exists();
//	}

//	private BooleanExpression isBlocked(Long loginUserId, String targetUsername) {
//		return JPAExpressions
//			.selectFrom(block)
//			.where(block.member.username.eq(targetUsername).and(
//				block.blockMember.id.eq(loginUserId)))
//			.exists();
//	}

}
