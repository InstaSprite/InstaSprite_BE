package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class EmailVerifyTokenInvalidException extends BusinessException {
    public EmailVerifyTokenInvalidException() {
        super(ErrorCode.EMAIL_VERIFY_TOKEN_INVALID);
    }
}


