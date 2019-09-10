package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.dal.TestValidationDAO;
import com.sj.application.framework.validation.dal.TestValidationStepDAO;
import com.sj.application.framework.validation.model.TestValidationData;
import com.sj.application.framework.validation.model.TestValidationResponse;
import com.sj.application.framework.validation.model.ValidatorResponse;

@Service
public class GetTestValidationService {

	@Inject
	private TestValidationDAO testValidationDao;

	@Inject
	private TestValidationStepDAO testValidationStepDao;
	
	/**
	 * This method retrieves status of an existing validation request
	 * 
	 * @param validationId
	 * @return
	 */
	public TestValidationResponse getTestValidationStatus(Long validationId) {
		TestValidationResponse response = new TestValidationResponse();
		
		// 1. validate request
		ValidatorResponse validatorResponse = validateRequest(validationId);
		if (!validatorResponse.isValid()) {
			response.setErrorMessage(validatorResponse.getErrorMessage());
			return response;
		}

		// 2. get test-validation data
		TestValidationData validationRequest = testValidationDao.getValidationById(validationId);
		response.setData(validationRequest);

		// 3. if valid, then load steps
		if (validationRequest != null && validationRequest.getValidationId() != null) {
			validationRequest.setSteps(testValidationStepDao.getSteps(validationRequest.getValidationId()));
		}
		
		return response;
	}
	
	private ValidatorResponse validateRequest(Long validationId) {
		ValidatorResponse response = new ValidatorResponse();
		
		if (validationId == null || validationId == 0) {
			response.addError("validationId is mandatory.");
		}
		
		return response;
	}
}
