package com.sj.application.framework.validation.model;

/**
 * 
 * List of validation statuses
 *
 */
public enum ValidationStatus {
	UNKNOWN, EXECUTING, SUCCESS, FAILED;

	public boolean isCompleted() {
		return this == SUCCESS || this == FAILED;
	}

	public boolean isRunning() {
		return this == EXECUTING;
	}
	
	public String getText() {
		return name();
	}
	
	public static ValidationStatus getEnum(String text) {
		for (ValidationStatus value : values()) {
			if (value.getText().equals(text)) {
				return value;
			}
		}
		return UNKNOWN;
	}
}
