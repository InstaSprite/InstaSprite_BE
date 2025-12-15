package org.olaz.instasprite_be.domain.member.service;

import static org.olaz.instasprite_be.global.error.ErrorCode.*;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.follow.repository.FollowRepository;
import org.olaz.instasprite_be.domain.member.entity.Block;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.exception.BlockMyselfFailException;
import org.olaz.instasprite_be.domain.member.exception.UnblockFailException;
import org.olaz.instasprite_be.domain.member.exception.UnblockMyselfFailException;
import org.olaz.instasprite_be.domain.member.repository.BlockRepository;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.global.error.exception.EntityAlreadyExistException;
import org.olaz.instasprite_be.global.error.exception.EntityNotFoundException;
import org.olaz.instasprite_be.global.util.AuthUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockService {

	private final AuthUtil authUtil;
	private final BlockRepository blockRepository;
	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;

	@Transactional
	public boolean block(String blockMemberUsername) {
		final Member member = authUtil.getLoginMember();
		final Member blockMember = memberRepository.findByUsername(blockMemberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

		if (member.getId().equals(blockMember.getId())) {
			throw new BlockMyselfFailException();
		}
		if (blockRepository.existsByMemberIdAndBlockMemberId(member.getId(), blockMember.getId())) {
			throw new EntityAlreadyExistException(BLOCK_ALREADY_EXISTS);
		}

		followRepository.findByMemberIdAndFollowMemberId(member.getId(), blockMember.getId())
			.ifPresent(followRepository::delete);

		followRepository.findByMemberIdAndFollowMemberId(blockMember.getId(), member.getId())
			.ifPresent(followRepository::delete);

		final Block block = new Block(member, blockMember);
		blockRepository.save(block);
		return true;
	}

	@Transactional
	public boolean unblock(String blockMemberUsername) {
		final Long memberId = authUtil.getLoginMemberId();
		final Member blockMember = memberRepository.findByUsername(blockMemberUsername)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

		if (memberId.equals(blockMember.getId())) {
			throw new UnblockMyselfFailException();
		}

		final Block block = blockRepository.findByMemberIdAndBlockMemberId(memberId, blockMember.getId())
			.orElseThrow(UnblockFailException::new);
		blockRepository.delete(block);
		return true;
	}

}
