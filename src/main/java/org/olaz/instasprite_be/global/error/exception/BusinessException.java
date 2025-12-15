package org.olaz.instasprite_be.global.error.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.ErrorResponse;


@Getter
public class BusinessException extends RuntimeException {

	private ErrorCode errorCode;
	private List<ErrorResponse.FieldError> errors = new ArrayList<>();

	public BusinessException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
		super(errorCode.getMessage());
		this.errors = errors;
		this.errorCode = errorCode;
	}
}