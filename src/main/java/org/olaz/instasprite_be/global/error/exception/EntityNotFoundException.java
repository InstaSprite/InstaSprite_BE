package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
