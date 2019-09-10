package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.model.CreateTestValidationRequest;
import com.sj.application.framework.validation.model.ReplayTestValidationRequest;
import com.sj.application.framework.validation.model.TestValidationResponse;
import com.sj.application.framework.validation.model.ValidationRunRequest;
import com.sj.application.framework.validation.model.ValidationRunResponse;

/**
 * 
 * Facade over various services that do bulk of the work
 * <p>
 * <li>Method to create new run entry in database</li>
 * <li>Method to start validation</li>
 * <li>Method to get validation status of an existing run</li>
 * <li>Method to replay validation</li>
 */
@Service
public class ValidationService {
	
	@Inject
	private CreateValidationRunService createValidationRunService;
	
	@Inject
	private GetValidationRunService getValidationRunService;
	
	@Inject
	private CreateTestValidationService createTestValidationService;
	
	@Inject
	private ReplayTestValidationService replayTestValidationService;
	
	@Inject
	private GetTestValidationService getTestValidationService;

	public ValidationRunResponse createRunEntry(ValidationRunRequest request) {
		return createValidationRunService.createRunEntry(request);
	}
	
	public ValidationRunResponse getRun(Long runId) {
		return getValidationRunService.getRun(runId);
	}

	public TestValidationResponse createTestValidation(CreateTestValidationRequest request) {
		return createTestValidationService.createTestValidation(request);
	}
	
	public TestValidationResponse getTestValidationStatus(Long validationId) {
		return getTestValidationService.getTestValidationStatus(validationId);
	}

	public TestValidationResponse replayTestValidation(ReplayTestValidationRequest request) {
		return replayTestValidationService.replayTestValidation(request);
	}

}
