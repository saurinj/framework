package com.sj.application.framework.step;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

/**
 * 
 * This is a default implementation of ParallelStepGroup - which executes
 * enclosing steps in parallel manner.
 * <p>
 * This class holds list of steps to be executed in parallel. execute() method
 * then executes all the steps held by this class in concurrent fashion
 *
 */
public class ParallelStepGroup<T> extends AbstractParallelStepGroup<T> {

	private List<ParallelStep<T>> steps;

	public ParallelStepGroup(ExecutorService taskExecutor, List<ParallelStep<T>> steps) {
		super(getName(steps), taskExecutor);
		Assert.notNull(steps, "steps can not be null");
		this.steps = steps;
	}
	
	private static <T> String getName(List<ParallelStep<T>> steps) {
		return "ParallelStepGroup["
				+ steps.stream().map(step -> step.getStepData().getStepName()).collect(Collectors.joining(",")) + "]";
	}

	@Override
	public List<ParallelStep<T>> getSteps() {
		return steps;
	}

}
