package com.sj.application.framework.validation.model;

import java.util.Date;
import java.util.List;

import com.sj.application.framework.step.Step;

/**
 * POJO that contains details about test validation
 *
 */
public class TestValidationData {

	private Long validationId;
	private ValidationRunData runData;
	private String uniqueKey;
	private ValidationStatus validationStatus;
	private List<Step<TestValidationData>> steps;
	private TestValidationData validationComparisonData;
	private TestValidationData replayData;
	private String cassandraTcId;
	private Date timeCreated;
	private Date timeUpdated;
	
	public Long getValidationId() {
		return validationId;
	}
	
	public void setValidationId(Long validationId) {
		this.validationId = validationId;
	}
	
	public ValidationRunData getRunData() {
		return runData;
	}
	
	public void setRunData(ValidationRunData runData) {
		this.runData = runData;
	}
	
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
	public boolean isAlreadyRunning() {
		return validationStatus.isRunning();
	}
	
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}
	
	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
	}
	
	public ValidationStatus computeTerminalStatus() {
		if (steps.stream().allMatch(step -> step.getStepData().getStepStatus().isSuccess())) {
			return ValidationStatus.SUCCESS;
		} else {
			return ValidationStatus.FAILED;
		}
	}
	
	public List<Step<TestValidationData>> getSteps() {
		return steps;
	}
	
	public void setSteps(List<Step<TestValidationData>> steps) {
		this.steps = steps;
	}
	
	public TestValidationData getValidationComparisonData() {
		return validationComparisonData;
	}
	
	public void setValidationComparisonData(TestValidationData validationComparisonData) {
		this.validationComparisonData = validationComparisonData;
	}
	
	public TestValidationData getReplayData() {
		return replayData;
	}
	
	public void setReplayData(TestValidationData replayData) {
		this.replayData = replayData;
	}
	
	public String getCassandraTcId() {
		return cassandraTcId;
	}
	
	public void setCassandraTcId(String cassandraTcId) {
		this.cassandraTcId = cassandraTcId;
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
