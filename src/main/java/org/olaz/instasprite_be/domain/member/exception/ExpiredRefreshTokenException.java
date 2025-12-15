package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class ExpiredRefreshTokenException extends BusinessException {
	public ExpiredRefreshTokenException() {
		super(ErrorCode.EXPIRED_REFRESH_TOKEN);
	}

}
