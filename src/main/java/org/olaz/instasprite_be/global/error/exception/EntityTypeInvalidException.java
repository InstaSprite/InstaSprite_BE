package org.olaz.instasprite_be.global.error.exception;


import org.olaz.instasprite_be.global.error.ErrorCode;

public class EntityTypeInvalidException extends BusinessException {

	public EntityTypeInvalidException() {
		super(ErrorCode.ENTITY_TYPE_INVALID);
	}

}
