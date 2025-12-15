package org.olaz.instasprite_be.domain.feed.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class CantUploadReplyException extends BusinessException {

	public CantUploadReplyException() {
		super(ErrorCode.REPLY_CANT_UPLOAD);
	}

}
