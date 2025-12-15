package org.olaz.instasprite_be.domain.alarm.dto;

import lombok.*;

import org.olaz.instasprite_be.domain.alarm.entity.Alarm;
import org.olaz.instasprite_be.domain.member.dto.MemberDto;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AlarmFollowDto extends AlarmDto {

	private boolean isFollowing;

	public AlarmFollowDto(Alarm alarm, boolean isFollowing) {
		super(alarm.getId(), alarm.getType().name(), alarm.getType().getMessage(), new MemberDto(alarm.getAgent()),
			alarm.getCreatedDate());
		this.isFollowing = isFollowing;
	}

}
