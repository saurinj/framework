package com.sj.application.framework.step;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 
 * Utility class that helps build Steps
 *
 */
public class StepBuilder {

	public static <T> List<Step<T>> buildSteps(List<Class<? extends Step<T>>> stepClasses, ExecutorService taskExecutor) {
		List<Step<T>> steps = new ArrayList<>();
		int i = 0;
		while (i < stepClasses.size()) {
			Step<T> step = getInstance(stepClasses.get(i));
			// group consecutive ParallelStep in ParallelStepGroup so that they can be executed in parallel
			List<ParallelStep<T>> parallelSteps = new ArrayList<>();
			if (step instanceof ParallelStep) {
				do {
					parallelSteps.add((ParallelStep<T>) step);
					i++;
					step = i < stepClasses.size() ? getInstance(stepClasses.get(i)) : null;
				} while (step instanceof ParallelStep);
				// if the Parallel Task is surrounded by sequential task, then treat it as a sequential task
				if (parallelSteps.size() == 1) {
					steps.add(step);
				} else {
					steps.add(createParallelStepGroup(taskExecutor, parallelSteps));
				}
			} else {
				steps.add(step);
			}
			i++;
		}
		return steps;
	}

	private static <T> Step<T> getInstance(Class<? extends Step<T>> stepClass) {
		try {
			return stepClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Excepting creating instance of step", e);
		}
	}

	private static <T> Step<T> createParallelStepGroup(ExecutorService taskExecutor, List<ParallelStep<T>> parallelSteps) {
		return new ParallelStepGroup<T>(taskExecutor, parallelSteps);
	}
}
