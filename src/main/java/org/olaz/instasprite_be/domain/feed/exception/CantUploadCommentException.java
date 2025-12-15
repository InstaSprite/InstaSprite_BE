package org.olaz.instasprite_be.domain.feed.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class CantUploadCommentException extends BusinessException {

	public CantUploadCommentException() {
		super(ErrorCode.COMMENT_CANT_UPLOAD);
	}

}
