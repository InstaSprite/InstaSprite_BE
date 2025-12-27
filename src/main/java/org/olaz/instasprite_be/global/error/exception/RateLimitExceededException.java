package org.olaz.instasprite_be.global.error.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;

public class RateLimitExceededException extends BusinessException {
    public RateLimitExceededException() {
        super(ErrorCode.RATE_LIMIT_EXCEEDED);
    }
}


