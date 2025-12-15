package org.olaz.instasprite_be.domain.member.repository.querydsl;

import org.olaz.instasprite_be.domain.member.dto.MiniProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.UserProfileResponse;

public interface MemberRepositoryQuerydsl {

	UserProfileResponse findUserProfileByLoginMemberIdAndTargetUsername(Long loginMemberId, String username);

	MiniProfileResponse findMiniProfileByLoginMemberIdAndTargetUsername(Long loginMemberId, String username);

}
