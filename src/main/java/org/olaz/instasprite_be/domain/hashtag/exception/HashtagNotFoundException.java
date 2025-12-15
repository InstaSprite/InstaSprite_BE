package org.olaz.instasprite_be.domain.hashtag.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class HashtagNotFoundException extends BusinessException {

	public HashtagNotFoundException() {
		super(ErrorCode.HASHTAG_NOT_FOUND);
	}

}