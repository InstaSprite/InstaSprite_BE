package org.olaz.instasprite_be.domain.follow.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class FollowMyselfFailException extends BusinessException {

	public FollowMyselfFailException() {
		super(ErrorCode.FOLLOW_MYSELF_FAIL);
	}

}
