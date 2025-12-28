package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class PasswordResetTokenInvalidException extends BusinessException {
    public PasswordResetTokenInvalidException() {
        super(ErrorCode.PASSWORD_RESET_TOKEN_INVALID);
    }
}

