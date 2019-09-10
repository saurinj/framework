package com.sj.application.framework.step;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.sj.application.framework.step.StepEvent.StepEventType;
import com.sj.application.framework.validation.service.TestValidationExecutionService;

/**
 * 
 * This class executes steps in sequential order
 * <p>
 * It also publishes step status after each step event
 * 
 * @see TestValidationExecutionService
 *
 */
@Component
public class StepExecutor implements ApplicationEventPublisherAware {
	
	private ApplicationEventPublisher publisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	public <T> void execute(Context<T> context, List<? extends Step<T>> steps) {
		for (Step<T> step : steps) {
			context.addStep(step);
			// mark step status to executing
			publisher.publishEvent(new StepEvent<T>(this, StepEventType.STARTED, context, step));
			
			// execute
			step.execute(context);
			
			// mark step status to completed
			publisher.publishEvent(new StepEvent<T>(this, StepEventType.COMPLETED, context, step));
		}
	}
	
}
