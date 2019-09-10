package com.sj.application.framework.validation.model;

/**
 * 
 * POJO that holds output test validation response
 *
 */
public class TestValidationResponse {
	
	private TestValidationData data;
	private String errorMessage;

	public TestValidationData getData() {
		return data;
	}
	
	public void setData(TestValidationData data) {
		this.data = data;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
