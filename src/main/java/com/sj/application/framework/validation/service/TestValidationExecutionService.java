package com.sj.application.framework.validation.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sj.application.framework.step.Context;
import com.sj.application.framework.step.ParallelStep;
import com.sj.application.framework.step.Step;
import com.sj.application.framework.step.StepBuilder;
import com.sj.application.framework.step.StepEvent;
import com.sj.application.framework.step.StepExecutor;
import com.sj.application.framework.validation.dal.TestValidationDAO;
import com.sj.application.framework.validation.model.TestValidationData;
import com.sj.application.framework.validation.model.ValidationStatus;
import com.sj.application.framework.validation.model.ValidationType;

/**
 * 
 * This class gets list of steps using {@link ValidationType} and then executes
 * the validation steps.
 * <p>
 * Validation framework understands {@link Step} and {@link ParallelStep}
 * 
 * @see StepEvent
 * @see StepExecutor
 *
 */
@Service
public class TestValidationExecutionService {

	@Inject
	private StepExecutor stepExecutor;

	@Inject
	private ExecutorService taskExecutor;

	@Inject
	private TestValidationDAO testValidationDao;

	public void execute(TestValidationData request) {
		// build steps
		List<Step<TestValidationData>> steps = buildSteps(request);
		request.setSteps(steps);

		// mark status to EXECUTING
		request.setValidationStatus(ValidationStatus.EXECUTING);
		testValidationDao.updateStatus(request);

		// start execution
		stepExecutor.execute(new Context<TestValidationData>(request), steps);

		// mark status SUCCESS/FAILED
		request.setValidationStatus(request.computeTerminalStatus());
		testValidationDao.updateStatus(request);
	}

	private List<Step<TestValidationData>> buildSteps(TestValidationData request) {
		return StepBuilder.buildSteps(request.getRunData().getValidationType().getStepClasses(), taskExecutor);
	}

}
