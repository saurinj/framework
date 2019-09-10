package com.sj.application.framework.validation.model;

/**
 * 
 * POJO that holds input validation request for CREATE call
 *
 */
public class CreateTestValidationRequest {
	
	private Long runId;
	private String uniqueKey;
	
	public Long getRunId() {
		return runId;
	}
	
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
}
