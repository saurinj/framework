package com.sj.application.framework.step;

/**
 * 
 * A smallest unit of execution.
 *
 */
public interface Step<T> {

	void execute(Context<T> context);
	
	StepData getStepData();
}
