package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class TotpAlreadyEnabledException extends BusinessException {
    public TotpAlreadyEnabledException() {
        super(ErrorCode.TOTP_ALREADY_ENABLED);
    }
}


