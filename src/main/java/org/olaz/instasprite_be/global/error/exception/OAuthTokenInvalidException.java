package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class OAuthTokenInvalidException extends BusinessException {

	public OAuthTokenInvalidException() {
		super(ErrorCode.OAUTH_TOKEN_INVALID);
	}

	public OAuthTokenInvalidException(String message) {
		super(message, ErrorCode.OAUTH_TOKEN_INVALID);
	}

}

