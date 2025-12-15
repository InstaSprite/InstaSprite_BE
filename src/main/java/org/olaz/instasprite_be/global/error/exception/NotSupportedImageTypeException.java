package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class NotSupportedImageTypeException extends BusinessException {
	public NotSupportedImageTypeException() {
		super(ErrorCode.IMAGE_TYPE_NOT_SUPPORTED);
	}
}
