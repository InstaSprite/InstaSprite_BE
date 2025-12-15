package org.olaz.instasprite_be.domain.feed.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class CantDeletePostException extends BusinessException {

	public CantDeletePostException() {
		super(ErrorCode.POST_CANT_DELETE);
	}

}
