package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class TotpNotEnrolledException extends BusinessException {
    public TotpNotEnrolledException() {
        super(ErrorCode.TOTP_NOT_ENROLLED);
    }
}


