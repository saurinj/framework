package com.sj.application.framework.step;

/**
 * 
 * Default exception thrown if something goes wrong during step execution
 *
 */
public class StepException extends Exception {

	private static final long serialVersionUID = 1L;

	public StepException(String message) {
		super(message);
	}

	public StepException(String message, Throwable cause) {
		super(message, cause);
	}

	public StepException(Throwable cause) {
		super(cause);
	}
}
