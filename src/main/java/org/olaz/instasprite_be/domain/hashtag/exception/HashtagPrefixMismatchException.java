package org.olaz.instasprite_be.domain.hashtag.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class HashtagPrefixMismatchException extends BusinessException {

	public HashtagPrefixMismatchException() {
		super(ErrorCode.HASHTAG_PREFIX_MISMATCH);
	}

}
