package org.olaz.instasprite_be.domain.member.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class ProviderMismatchException extends BusinessException {
    public ProviderMismatchException() {
        super(ErrorCode.ACCOUNT_PROVIDER_MISMATCH);
    }
}

