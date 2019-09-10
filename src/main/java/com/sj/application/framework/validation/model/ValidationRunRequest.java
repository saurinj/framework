package com.sj.application.framework.validation.model;

/**
 * 
 * POJO that holds input run request
 *
 */
public class ValidationRunRequest {

	/**
	 * It says if the validation type is BASELINE or REGRESSION
	 */
	private ValidationType validationType;
	/**
	 * This field is optional by default.
	 * <p>
	 * It's required only when the test needs comparison with another run
	 */
	private Long comparisonRunId;
	
	private String startedBy;

	public ValidationType getValidationType() {
		return validationType;
	}

	public void setValidationType(ValidationType validationType) {
		this.validationType = validationType;
	}
	
	public Long getComparisonRunId() {
		return comparisonRunId;
	}
	
	public void setComparisonRunId(Long comparisonRunId) {
		this.comparisonRunId = comparisonRunId;
	}
	
	public String getStartedBy() {
		return startedBy;
	}
	
	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}

}
