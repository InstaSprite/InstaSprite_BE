package org.olaz.instasprite_be.domain.follow.service;

import static org.olaz.instasprite_be.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.alarm.service.AlarmService;
import org.olaz.instasprite_be.domain.follow.dto.FollowerDto;
import org.olaz.instasprite_be.domain.follow.entity.Follow;
import org.olaz.instasprite_be.domain.follow.exception.FollowMyselfFailException;
import org.olaz.instasprite_be.domain.follow.exception.FollowerDeleteFailException;
import org.olaz.instasprite_be.domain.follow.exception.UnfollowFailException;
import org.olaz.instasprite_be.domain.follow.exception.UnfollowMyselfFailException;
import org.olaz.instasprite_be.domain.follow.repository.FollowRepository;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.repository.BlockRepository;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.EntityAlreadyExistException;
import org.olaz.instasprite_be.global.error.exception.EntityNotFoundException;
import org.olaz.instasprite_be.global.util.AuthUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final MemberRepository memberRepository;
	private final BlockRepository blockRepository;
	private final AlarmService alarmService;
	private final AuthUtil authUtil;

	@Transactional
	public boolean follow(String followMemberUsername) {
		final Long memberId = authUtil.getLoginMemberId();
		final Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
		final Member followMember = memberRepository.findByUsername(followMemberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

		if (member.getId().equals(followMember.getId())) {
			throw new FollowMyselfFailException();
		}
		if (followRepository.existsByMemberIdAndFollowMemberId(member.getId(), followMember.getId())) {
			throw new EntityAlreadyExistException(ErrorCode.FOLLOW_ALREADY_EXIST);
		}

		if (blockRepository.existsByMemberIdAndBlockMemberId(followMember.getId(), member.getId())) {
			return false;
		}


		blockRepository.findByMemberIdAndBlockMemberId(member.getId(), followMember.getId())
			.ifPresent(blockRepository::delete);

		final Follow follow = new Follow(member, followMember);
		followRepository.save(follow);
		alarmService.alert(followMember, follow);

		return true;
	}

	@Transactional
	public boolean unfollow(String followMemberUsername) {
		final Long memberId = authUtil.getLoginMemberId();
		final Member followMember = memberRepository.findByUsername(followMemberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
		if (memberId.equals(followMember.getId())) {
			throw new UnfollowMyselfFailException();
		}
		final Follow follow = followRepository
			.findByMemberIdAndFollowMemberId(memberId, followMember.getId())
			.orElseThrow(UnfollowFailException::new);
		alarmService.delete(followMember, follow);
		followRepository.delete(follow);

		return true;
	}

	@Transactional
	public boolean deleteFollower(String followMemberUsername) {
		final Long memberId = authUtil.getLoginMemberId();
		final Member followMember = memberRepository.findByUsername(followMemberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
		if (memberId.equals(followMember.getId())) {
			throw new FollowerDeleteFailException();
		}
		final Follow follow = followRepository
			.findByMemberIdAndFollowMemberId(followMember.getId(), memberId)
			.orElseThrow(FollowerDeleteFailException::new);
		followRepository.delete(follow);
		return true;
	}

	@Transactional(readOnly = true)
	public List<FollowerDto> getFollowings(String memberUsername) {
		final Long memberId = authUtil.getLoginMemberId();

		final Member toMember = memberRepository.findByUsername(memberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

		final List<FollowerDto> result = followRepository.findFollowings(memberId, toMember.getId());
		return result;
	}

	@Transactional(readOnly = true)
	public List<FollowerDto> getFollowers(String memberUsername) {
		final Long memberId = authUtil.getLoginMemberId();

		final Member toMember = memberRepository.findByUsername(memberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

		final List<FollowerDto> result = followRepository.findFollowers(memberId, toMember.getId());
		return result;
	}

	@Transactional(readOnly = true)
	public List<Follow> getFollowings(Member member) {
		return followRepository.findAllByMember(member);
	}


}
