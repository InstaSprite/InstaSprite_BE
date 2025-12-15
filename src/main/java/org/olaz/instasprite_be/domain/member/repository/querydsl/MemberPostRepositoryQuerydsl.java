package org.olaz.instasprite_be.domain.member.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.olaz.instasprite_be.domain.feed.dto.MemberPostDto;

public interface MemberPostRepositoryQuerydsl {

	Page<MemberPostDto> findMemberPostDtoPageByLoginMemberIdAndTargetUsername(Long loginMemberId, String username, Pageable pageable);

	Page<MemberPostDto> findMemberSavedPostDtoPageByLoginMemberId(Long loginMemberId, Pageable pageable);

	Page<MemberPostDto> findMemberTaggedPostDtoPageByLoginMemberIdAndTargetUsername(Long loginMemberId, String username, Pageable pageable);

}
