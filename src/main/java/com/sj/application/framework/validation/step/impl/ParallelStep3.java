package com.sj.application.framework.validation.step.impl;

import com.sj.application.framework.step.Context;
import com.sj.application.framework.step.ParallelStep;
import com.sj.application.framework.step.StepException;
import com.sj.application.framework.validation.model.TestValidationData;

public class ParallelStep3 extends TestValidationStep
		implements ParallelStep<TestValidationData> {

	public ParallelStep3() {
		super("PAS_CHARGE_REQUEST_PLUGIN_VALIDATION_STEP");
	}

	@Override
	public void doExecute(Context<TestValidationData> context) throws StepException {

	}

}
