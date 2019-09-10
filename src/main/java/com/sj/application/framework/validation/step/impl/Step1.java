package com.sj.application.framework.validation.step.impl;

import com.sj.application.framework.step.Context;
import com.sj.application.framework.step.StepException;
import com.sj.application.framework.validation.model.TestValidationData;

public class Step1 extends TestValidationStep {

	public Step1() {
		super("ASYNC_COMPLETED_CHECK_STEP");
	}

	@Override
	public void doExecute(Context<TestValidationData> context) throws StepException {
		
		
	}

}
