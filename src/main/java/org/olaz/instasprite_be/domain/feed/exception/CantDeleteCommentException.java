package org.olaz.instasprite_be.domain.feed.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class CantDeleteCommentException extends BusinessException {

	public CantDeleteCommentException() {
		super(ErrorCode.COMMENT_CANT_DELETE);
	}

}
