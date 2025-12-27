package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class TotpRequiredException extends BusinessException {
    public TotpRequiredException() {
        super(ErrorCode.TOTP_REQUIRED);
    }
}


