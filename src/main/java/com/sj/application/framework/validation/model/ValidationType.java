package com.sj.application.framework.validation.model;

import java.util.Arrays;
import java.util.List;

import com.sj.application.framework.step.Step;
import com.sj.application.framework.validation.step.impl.ParallelStep1;
import com.sj.application.framework.validation.step.impl.ParallelStep2;
import com.sj.application.framework.validation.step.impl.ParallelStep3;
import com.sj.application.framework.validation.step.impl.Step1;
import com.sj.application.framework.validation.step.impl.Step2;
import com.sj.application.framework.validation.step.impl.Step3;

/**
 * 
 * This class holds steps to be performed for given validation type
 * <p>
 * If you want to run few steps in parallel, make sure you specify those
 * validation adjacently.
 * <p>
 * If there is only 1 ParallelStep is defined (surrounded by standard Step),
 * then it will be executed in serial manner
 *
 */
public enum ValidationType {

	UNKNOWN(Arrays.asList()), 
	DEFAULT_BASELINE(Arrays.asList(Step1.class, Step2.class, ParallelStep1.class,
			ParallelStep2.class, ParallelStep3.class)), 
	DEFAULT_REGRESSION(Arrays.asList(Step1.class, Step2.class, ParallelStep1.class, ParallelStep2.class,
			ParallelStep3.class, Step3.class));

	private List<Class<? extends Step<TestValidationData>>> stepClasses;

	private ValidationType(List<Class<? extends Step<TestValidationData>>> stepClasses) {
		this.stepClasses = stepClasses;
	}

	public List<Class<? extends Step<TestValidationData>>> getStepClasses() {
		return stepClasses;
	}

	public String getText() {
		return name();
	}

	public static ValidationType getEnum(String text) {
		for (ValidationType value : values()) {
			if (value.getText().equals(text)) {
				return value;
			}
		}
		return UNKNOWN;
	}

}
