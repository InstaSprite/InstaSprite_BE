package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class MemberDoesNotExistException extends BusinessException {
	public MemberDoesNotExistException() {
		super(ErrorCode.MEMBER_NOT_FOUND);
	}

}

