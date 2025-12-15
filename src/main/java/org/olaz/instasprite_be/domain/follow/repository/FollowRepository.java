package org.olaz.instasprite_be.domain.follow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.olaz.instasprite_be.domain.follow.entity.Follow;
import org.olaz.instasprite_be.domain.follow.repository.querydsl.FollowRepositoryQuerydsl;
import org.olaz.instasprite_be.domain.member.entity.Member;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryQuerydsl {

	public boolean existsByMemberIdAndFollowMemberId(Long memberId, Long followMemberId);

	public Optional<Follow> findByMemberIdAndFollowMemberId(Long memberId, Long followMemberId);

	public List<Follow> findAllByMemberId(Long memberId);

	public List<Follow> findAllByFollowMemberId(Long followMemberId);

	public int countByMemberId(Long memberId);

	public int countByFollowMemberId(Long followMemberId);

    List<Follow> findAllByMember(Member member);

}
