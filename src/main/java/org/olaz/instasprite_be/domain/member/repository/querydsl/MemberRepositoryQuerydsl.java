package org.olaz.instasprite_be.domain.member.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.dto.MiniProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.UserProfileResponse;

public interface MemberRepositoryQuerydsl {

	UserProfileResponse findUserProfileByLoginMemberIdAndTargetUsername(Long loginMemberId, String username);

	MiniProfileResponse findMiniProfileByLoginMemberIdAndTargetUsername(Long loginMemberId, String username);

	Page<MemberDto> searchMemberDtoByUsernameContains(String username, Pageable pageable);

}
