package com.sj.application.framework.step;

/**
 * 
 * Various status of step before/during/after execution
 *
 */
public enum StepStatus {
	UNKNOWN, EXECUTING, SUCCESS, FAILED;

	public boolean isCompleted() {
		return isSuccess() || isFailed();
	}

	public boolean isFailed() {
		return this == FAILED;
	}

	public boolean isSuccess() {
		return this == SUCCESS;
	}

	public boolean isRunning() {
		return this == EXECUTING;
	}
	
	public String getText() {
		return name();
	}
	
	public static StepStatus getEnum(String text) {
		for (StepStatus value : values()) {
			if (value.getText().equals(text)) {
				return value;
			}
		}
		return UNKNOWN;
	}
}
