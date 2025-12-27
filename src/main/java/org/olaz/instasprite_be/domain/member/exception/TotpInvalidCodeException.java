package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class TotpInvalidCodeException extends BusinessException {
    public TotpInvalidCodeException() {
        super(ErrorCode.TOTP_INVALID);
    }
}


