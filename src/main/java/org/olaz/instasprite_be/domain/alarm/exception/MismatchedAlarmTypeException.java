package org.olaz.instasprite_be.domain.alarm.exception;

import org.olaz.instasprite_be.global.error.ErrorCode;
import org.olaz.instasprite_be.global.error.exception.BusinessException;

public class MismatchedAlarmTypeException extends BusinessException {

	public MismatchedAlarmTypeException() {
		super(ErrorCode.MISMATCHED_ALARM_TYPE);
	}

}
