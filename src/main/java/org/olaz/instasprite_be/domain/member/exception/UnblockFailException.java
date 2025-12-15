package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class UnblockFailException extends BusinessException {
	public UnblockFailException() {
		super(ErrorCode.UNBLOCK_FAILED);
	}

}