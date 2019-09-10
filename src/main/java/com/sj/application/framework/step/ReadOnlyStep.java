package com.sj.application.framework.step;

/**
 * 
 * As the name suggest, this class creates ReadOnly step that will be returned
 * during GET call
 *
 */
public class ReadOnlyStep<T> extends AbstractStep<T> {

	public ReadOnlyStep(String stepName) {
		super(stepName);
	}

	@Override
	public void doExecute(Context<T> context) throws StepException {
		throw new StepException("This is a readonly step which can't be executed");

	}

}
