package org.olaz.instasprite_be.domain.feed.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class BookmarkMyselfFailException extends BusinessException {

	public BookmarkMyselfFailException() {
		super(ErrorCode.BOOKMARK_MYSELF_FAIL);
	}
}

