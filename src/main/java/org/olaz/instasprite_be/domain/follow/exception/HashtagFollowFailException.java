package org.olaz.instasprite_be.domain.follow.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class HashtagFollowFailException extends BusinessException {

	public HashtagFollowFailException() {
		super(ErrorCode.HASHTAG_FOLLOW_ALREADY_EXIST);
	}

}
