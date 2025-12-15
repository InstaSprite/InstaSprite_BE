package org.olaz.instasprite_be.domain.alarm.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.olaz.instasprite_be.domain.member.dto.MemberDto;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AlarmDto {

	private Long id;
	private String type;
	private String message;
	private MemberDto agent;
	private LocalDateTime createdDate;

}
