package com.sj.application.framework.validation.model;

/**
 * 
 * POJO that holds input validation request for REPLAY call
 *
 */
public class ReplayTestValidationRequest {

	private Long validationId;

	public Long getValidationId() {
		return validationId;
	}
	
	public void setValidationId(Long validationId) {
		this.validationId = validationId;
	}
}
