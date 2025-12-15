package org.olaz.instasprite_be.domain.follow.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class UnfollowFailException extends BusinessException {

	public UnfollowFailException() {
		super(ErrorCode.UNFOLLOW_FAIL);
	}

}
