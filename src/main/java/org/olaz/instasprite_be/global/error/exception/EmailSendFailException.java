package org.olaz.instasprite_be.global.error.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;

public class EmailSendFailException extends BusinessException {
    public EmailSendFailException() {
        super(ErrorCode.EMAIL_SEND_FAIL);
    }
}