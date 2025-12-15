package org.olaz.instasprite_be.domain.hashtag.repository.querydsl;

import static org.olaz.instasprite_be.domain.follow.entity.QHashtagFollow.*;
import static org.olaz.instasprite_be.domain.hashtag.entity.QHashtag.*;
import static org.olaz.instasprite_be.domain.hashtag.entity.QHashtagPost.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.hashtag.dto.HashtagProfileResponse;
import org.olaz.instasprite_be.domain.hashtag.dto.QHashtagProfileResponse;

@RequiredArgsConstructor
public class HashtagPostRepositoryQuerydslImpl implements HashtagPostRepositoryQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public HashtagProfileResponse findHashtagProfileByLoginMemberIdAndHashtagId(Long loginMemberId, Long hashtagId) {
		return queryFactory
			.select(new QHashtagProfileResponse(
				hashtagPost.hashtag.name,
				hashtagPost.count(),
				isFollowingHashtag(loginMemberId, hashtagId)
			))
			.from(hashtagPost)
			.innerJoin(hashtagPost.hashtag, hashtag)
			.where(hashtagPost.hashtag.id.eq(hashtagId))
			.fetchOne();
	}

	@Override
	public HashtagProfileResponse findHashtagProfileByHashtagId(Long hashtagId) {
		return queryFactory
			.select(new QHashtagProfileResponse(
				hashtagPost.hashtag.name,
				hashtagPost.count()
			))
			.from(hashtagPost)
			.innerJoin(hashtagPost.hashtag, hashtag)
			.where(hashtagPost.hashtag.id.eq(hashtagId))
			.fetchOne();
	}

	private BooleanExpression isFollowingHashtag(Long loginMemberId, Long hashtagId) {
		return JPAExpressions
			.selectFrom(hashtagFollow)
			.where(hashtagFollow.hashtag.id.eq(hashtagId).and(hashtagFollow.member.id.eq(loginMemberId)))
			.exists();
	}

}
