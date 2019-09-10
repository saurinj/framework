package com.sj.application.framework.validation.step.impl;

import com.sj.application.framework.step.AbstractStep;
import com.sj.application.framework.validation.model.TestValidationData;

/**
 * 
 * Common abstract base implementation for all TestValidation steps.
 *
 */
public abstract class TestValidationStep extends AbstractStep<TestValidationData> {

	public TestValidationStep(String stepName) {
		super(stepName);
	}

}
