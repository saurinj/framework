package com.sj.application.framework.step;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.sj.application.framework.validation.service.TestValidationExecutionService;

/**
 * Allows loose coupling between {@link StepExecutor} and clients calling
 * StepExecutor.
 * <p>
 * The clients are notified of different StepEventType (such as when step
 * execution started, step execution completed etc).
 * <p>
 * For client to receive such events, they need to {@link ApplicationListener}
 * 
 * @see TestValidationExecutionService
 *
 */
public class StepEvent<T> extends ApplicationEvent {


	private static final long serialVersionUID = 1L;

	private Context<T> context;
	private Step<T> step;
	private StepEventType stepEventType;

	public StepEvent(Object source, StepEventType stepEventType, Context<T> context, Step<T> step) {
		super(source);
		this.stepEventType = stepEventType;
		this.context = context;
		this.step = step;
	}

	public StepEventType getStepEventType() {
		return stepEventType;
	}

	public Context<T> getContext() {
		return context;
	}

	public Step<T> getStep() {
		return step;
	}

	public static enum StepEventType {
		STARTED, COMPLETED;
	}

}
