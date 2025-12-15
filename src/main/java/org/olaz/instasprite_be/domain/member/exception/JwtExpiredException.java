package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class JwtExpiredException extends BusinessException {
	public JwtExpiredException() {
		super(ErrorCode.JWT_EXPIRED);
	}

}
