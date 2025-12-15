package org.olaz.instasprite_be.global.error.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.ErrorResponse;

import java.util.List;



public class InvalidInputException extends BusinessException {

	public InvalidInputException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.INPUT_VALUE_INVALID, errors);
	}

}
