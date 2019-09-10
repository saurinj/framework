package com.sj.application.framework.validation.step.impl;

import com.sj.application.framework.step.Context;
import com.sj.application.framework.step.StepException;
import com.sj.application.framework.validation.model.TestValidationData;

public class Step3 extends TestValidationStep {

	public Step3() {
		super("COMPARE_WITH_BASELINE_STEP");
	}

	@Override
	public void doExecute(Context<TestValidationData> context) throws StepException {

	}

}
