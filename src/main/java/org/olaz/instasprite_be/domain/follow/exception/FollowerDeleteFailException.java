package org.olaz.instasprite_be.domain.follow.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class FollowerDeleteFailException extends BusinessException {

	public FollowerDeleteFailException() {
		super(ErrorCode.FOLLOWER_DELETE_FAIL);
	}

}
