package com.sj.application.framework.step;

import java.util.Date;

/**
 * 
 * Holds data for the step
 *
 */
public class StepData {

	private Long id;
	private String stepName;
	private StepStatus stepStatus = StepStatus.UNKNOWN;
	private String message;
	private Date timeCreated;
	private Date timeUpdated;
	
	public StepData(String stepName) {
		this.stepName = stepName;
	}
	
	public String getStepName() {
		return stepName;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public StepStatus getStepStatus() {
		return stepStatus;
	}
	
	public void setStepStatus(StepStatus stepStatus) {
		this.stepStatus = stepStatus;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
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
