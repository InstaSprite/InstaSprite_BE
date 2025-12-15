package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class OAuthProviderException extends BusinessException {

	public OAuthProviderException() {
		super(ErrorCode.OAUTH_PROVIDER_ERROR);
	}

	public OAuthProviderException(String message) {
		super(message, ErrorCode.OAUTH_PROVIDER_ERROR);
	}

}

