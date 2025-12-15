package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class GoogleAuthFailException extends BusinessException {

	public GoogleAuthFailException() {
		super(ErrorCode.GOOGLE_AUTH_FAIL);
	}

	public GoogleAuthFailException(String message) {
		super(message, ErrorCode.GOOGLE_AUTH_FAIL);
	}

}

