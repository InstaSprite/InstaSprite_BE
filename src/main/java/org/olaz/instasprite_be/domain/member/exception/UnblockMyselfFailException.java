package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class UnblockMyselfFailException extends BusinessException {
	public UnblockMyselfFailException() {
		super(ErrorCode.UNBLOCK_MYSELF_FAIL);
	}

}