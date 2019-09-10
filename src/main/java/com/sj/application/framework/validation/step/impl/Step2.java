package com.sj.application.framework.validation.step.impl;

import com.sj.application.framework.step.Context;
import com.sj.application.framework.step.StepException;
import com.sj.application.framework.validation.model.TestValidationData;

public class Step2 extends TestValidationStep {

	public Step2() {
		super("SETTLEMENT_SOR_VALIDATION_STEP");
	}

	@Override
	public void doExecute(Context<TestValidationData> context) throws StepException {
		
		
	}
}
