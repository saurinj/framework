package com.sj.application.framework.validation.model;

import java.util.List;

/**
 * 
 * POJO that holds output run response, including runId etc
 *
 */
public class ValidationRunResponse {

	private ValidationRunData data;
	private List<TestValidationData> testValidationDataList;
	private String errorMessage;
	
	public ValidationRunData getData() {
		return data;
	}
	
	public void setData(ValidationRunData data) {
		this.data = data;
	}
	
	public List<TestValidationData> getTestValidationDataList() {
		return testValidationDataList;
	}
	
	public void setTestValidationDataList(List<TestValidationData> testValidationDataList) {
		this.testValidationDataList = testValidationDataList;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
