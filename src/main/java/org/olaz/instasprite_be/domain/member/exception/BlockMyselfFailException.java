package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class BlockMyselfFailException extends BusinessException {
	public BlockMyselfFailException() {
		super(ErrorCode.BLOCK_MYSELF_FAIL);
	}

}