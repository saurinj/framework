package com.sj.application.framework.step;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * An abstract implementation of ParallelStepGroup. It executes all steps
 * returned by getSteps() method in parallel.
 * 
 * @see ParallelStepGroup
 */

public abstract class AbstractParallelStepGroup<T> extends AbstractStep<T> {

	private ExecutorService taskExecutor;

	public AbstractParallelStepGroup(String stepName, ExecutorService taskExecutor) {
		super(stepName);
		Assert.notNull(taskExecutor, "taskExecutor can not be null");
		this.taskExecutor = taskExecutor;
	}

	public abstract List<ParallelStep<T>> getSteps();

	@Override
	public void doExecute(Context<T> context) throws StepException {
		getResults(context, executeAll(context));
		getStepData().setStepStatus(getStepStatus());
		getStepData().setMessage(getMessage());
	}

	private List<ParallelStepWrapper<T>> executeAll(Context<T> context) {
		List<ParallelStep<T>> steps = getSteps();
		List<ParallelStepWrapper<T>> stepWrappers = new ArrayList<>(steps.size());

		for (ParallelStep<T> step : steps) {
			ParallelStepWrapper<T> stepWrapper = new ParallelStepWrapper<T>(step, context);
			Future<?> future = taskExecutor.submit(stepWrapper);
			stepWrapper.setFuture(future);
			stepWrappers.add(stepWrapper);
		}

		return stepWrappers;
	}

	private void getResults(Context<T> context, List<ParallelStepWrapper<T>> stepWrappers) throws StepException {
		for (ParallelStepWrapper<T> stepWrapper : stepWrappers) {
			try {
				stepWrapper.getFuture().get(stepWrapper.getStep().getSlaInMillis(), TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				throw new StepException("Exception retrieving results from ParallelStepGroup", e);
			}
		}
	}

	protected StepStatus getStepStatus() {
		if (getSteps().stream().allMatch(step -> step.getStepData().getStepStatus().isSuccess())) {
			return StepStatus.SUCCESS;
		} else {
			return StepStatus.FAILED;
		}
	}

	protected String getMessage() {
		return getSteps().stream().filter(step -> StringUtils.isNotEmpty(step.getStepData().getMessage()))
				.map(step -> step.getStepData().getStepName() + ":" + step.getStepData().getMessage())
				.collect(Collectors.joining(","));
	}

	private static class ParallelStepWrapper<T> implements Runnable {

		private ParallelStep<T> step;
		private Context<T> context;
		private Future<?> future;

		public ParallelStepWrapper(final ParallelStep<T> step, final Context<T> context) {
			this.step = step;
			this.context = context;
		}

		public ParallelStep<T> getStep() {
			return step;
		}

		@Override
		public void run() {
			step.execute(context);
		}

		public Future<?> getFuture() {
			return future;
		}

		public void setFuture(Future<?> future) {
			this.future = future;
		}

	}
}
