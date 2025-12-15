package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class FileConvertFailException extends BusinessException {

	public FileConvertFailException() {
		super(ErrorCode.FILE_CONVERT_FAIL);
	}

}
