package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.dal.TestValidationDAO;
import com.sj.application.framework.validation.dal.ValidationRunDAO;
import com.sj.application.framework.validation.model.CreateTestValidationRequest;
import com.sj.application.framework.validation.model.TestValidationData;
import com.sj.application.framework.validation.model.TestValidationResponse;
import com.sj.application.framework.validation.model.ValidationRunData;
import com.sj.application.framework.validation.model.ValidationStatus;
import com.sj.application.framework.validation.model.ValidatorResponse;

@Service
public class CreateTestValidationService {
	
	@Inject
	private QueueService queueService;

	@Inject
	private ValidationRunDAO validationRunDao;

	@Inject
	private TestValidationDAO testValidationDao;

	/**
	 * This method creates new validation entry in database.
	 * <p>
	 * <li>It performs required validation in real-time</li>
	 * <li>If all validations are successful, it queues the validation request,
	 * which is executed in async manner</li>
	 * 
	 * @param request
	 * @return TestValidationResponse
	 */
	public TestValidationResponse createTestValidation(CreateTestValidationRequest request) {
		TestValidationResponse response = new TestValidationResponse();
		
		// 1. perform request validation
		ValidatorResponse validatorResponse = validateRequest(request);
		if (!validatorResponse.isValid()) {
			response.setErrorMessage(validatorResponse.getErrorMessage());
			return response;
		}

		// 2. Load existing run id
		ValidationRunData runData = validationRunDao.getRunData(request.getRunId());
		if (runData == null) {
			response.setErrorMessage("Invalid runId: " + request.getRunId());
			return response;
		}

		// 3. check if there is a comparison validation id exist (if applicable)
		TestValidationData validationComparisonData = null;
		if (runData.getComparisonRunData() != null) {
			validationComparisonData = testValidationDao.getLatestValidationByRunIdAndUniqueKey(
					runData.getComparisonRunData().getRunId(), request.getUniqueKey());
			if (validationComparisonData == null) {
				response.setErrorMessage("No active comparison validation id found for comparison runId: "
						+ runData.getComparisonRunData().getRunId() + " and uniqueKey:" + request.getUniqueKey());
				return response;
			}
		}

		// 4. check if there is an existing validation request exists for this input
		TestValidationData existingTestValidationRequest = testValidationDao
				.getLatestValidationByRunIdAndUniqueKey(request.getRunId(), request.getUniqueKey());
		if (existingTestValidationRequest != null) {
			response.setErrorMessage("ValidationId " + existingTestValidationRequest.getValidationId()
					+ " already exists for runId " + request.getRunId() + " and uniqueKey " + request.getUniqueKey());
			return response;
		}

		// 5. create entry in database table
		TestValidationData validationRequest = testValidationDao
				.createTestValidation(prepareRequest(request, runData, validationComparisonData));
		response.setData(validationRequest);
		if (validationRequest == null) {
			response.setErrorMessage("Unable to create entry in database at this time. Please try again later.");
			return response;
		}

		// 6. enqueue request for execution
		boolean accepted = queueService.publishToQueue(validationRequest);

		// 7. if queue is exhausted then delete the entry
		if (!accepted) {
			// delete an entry
			testValidationDao.deleteById(validationRequest.getValidationId());
			// and return error message
			response.setErrorMessage("Couldn't accept a new request due to server load. Queue is exhausted");
			response.setData(null);
		}

		return response;
	}
	
	private ValidatorResponse validateRequest(CreateTestValidationRequest request) {
		ValidatorResponse response = new ValidatorResponse();
		if (request.getRunId() == null || request.getRunId() == 0) {
			response.addError("runId is mandatory.");
		}
		
		if (StringUtils.isBlank(request.getUniqueKey())) {
			response.addError("uniqueKey is mandatory.");
		}
		
		return response;
	}

	private TestValidationData prepareRequest(CreateTestValidationRequest request, ValidationRunData runData,
			TestValidationData validationComparisonData) {
		TestValidationData testValidationData = new TestValidationData();
		testValidationData.setValidationStatus(ValidationStatus.UNKNOWN);
		testValidationData.setUniqueKey(request.getUniqueKey());
		testValidationData.setRunData(runData);
		testValidationData.setValidationComparisonData(validationComparisonData);
		return testValidationData;
	}
}
