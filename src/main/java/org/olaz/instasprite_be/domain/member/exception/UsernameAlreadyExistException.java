package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class UsernameAlreadyExistException extends BusinessException {
	public UsernameAlreadyExistException() {
		super(ErrorCode.USERNAME_ALREADY_EXISTS);
	}

}
