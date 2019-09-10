package com.sj.application.framework.step;

/**
 * 
 * Abstract implementation of {@link Step}. All step implementations should
 * extend this class and override doExecute() method
 *
 */
public abstract class AbstractStep<T> implements Step<T> {

	private StepData stepData;

	public AbstractStep(String stepName) {
		stepData = new StepData(stepName);
		stepData.setStepStatus(StepStatus.UNKNOWN);
	}
	
	public StepData getStepData() {
		return stepData;
	}

	@Override
	public void execute(Context<T> context) {
		try {
			stepData.setStepStatus(StepStatus.EXECUTING);
			doExecute(context);
			stepData.setStepStatus(StepStatus.SUCCESS);
		} catch (Exception e) {
			stepData.setStepStatus(StepStatus.FAILED);
			stepData.setMessage(e.getMessage());
		}
	}

	protected abstract void doExecute(Context<T> context) throws StepException;
}
