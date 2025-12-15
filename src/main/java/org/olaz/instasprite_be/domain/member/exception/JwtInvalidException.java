package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class JwtInvalidException extends BusinessException {
	public JwtInvalidException() {
		super(ErrorCode.JWT_INVALID);
	}

}
