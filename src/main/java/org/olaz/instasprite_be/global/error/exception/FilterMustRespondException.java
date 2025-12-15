package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class FilterMustRespondException extends BusinessException {

	public FilterMustRespondException() {
		super(ErrorCode.FILTER_MUST_RESPOND);
	}

}
