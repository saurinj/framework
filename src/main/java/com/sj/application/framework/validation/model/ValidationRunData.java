package com.sj.application.framework.validation.model;

import java.util.Date;

public class ValidationRunData {

	private Long runId;
	private ValidationType validationType;
	private ValidationRunData comparisonRunData;
	private String startedBy;
	private Date timeCreated;
	private Date timeUpdated;
	
	public Long getRunId() {
		return runId;
	}
	
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public ValidationType getValidationType() {
		return validationType;
	}
	
	public void setValidationType(ValidationType validationType) {
		this.validationType = validationType;
	}
	
	public ValidationRunData getComparisonRunData() {
		return comparisonRunData;
	}
	
	public void setComparisonRunData(ValidationRunData comparisonRunData) {
		this.comparisonRunData = comparisonRunData;
	}
	
	public String getStartedBy() {
		return startedBy;
	}
	
	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}
	
	public Date getTimeCreated() {
		return timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	public Date getTimeUpdated() {
		return timeUpdated;
	}
	
	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
}
