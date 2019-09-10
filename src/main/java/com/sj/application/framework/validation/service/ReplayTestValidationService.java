package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.dal.TestValidationDAO;
import com.sj.application.framework.validation.model.ReplayTestValidationRequest;
import com.sj.application.framework.validation.model.TestValidationData;
import com.sj.application.framework.validation.model.TestValidationResponse;
import com.sj.application.framework.validation.model.ValidationStatus;
import com.sj.application.framework.validation.model.ValidatorResponse;

@Service
public class ReplayTestValidationService {

	@Inject
	private QueueService queueService;

	@Inject
	private TestValidationDAO testValidationDao;

	/**
	 * This method replays an existing validation request.
	 * <p>
	 * <li>It performs required validation in real-time</li>
	 * <li>If all validations are successful, it queues the validation request,
	 * which is executed in async manner</li>
	 * 
	 * @param request
	 * @return
	 */
	public TestValidationResponse replayTestValidation(ReplayTestValidationRequest request) {
		TestValidationResponse response = new TestValidationResponse();

		// 1. perform request validation
		ValidatorResponse validatorResponse = validateRequest(request);
		if (!validatorResponse.isValid()) {
			response.setErrorMessage(validatorResponse.getErrorMessage());
			return response;
		}

		// 2. check if validation id is valid
		TestValidationData originalValidationRequest = testValidationDao.getValidationById(request.getValidationId());
		if (originalValidationRequest == null) {
			response.setErrorMessage("ValidationId " + request.getValidationId() + " doesn't exist");
			return response;
		}

		// 3. check if validation is already running
		if (originalValidationRequest.isAlreadyRunning()) {
			response.setErrorMessage("ValidationId " + request.getValidationId() + " is already executing");
			return response;
		}

		if (originalValidationRequest.getReplayData() != null
				&& originalValidationRequest.getReplayData().getValidationId() != null) {
			response.setErrorMessage(
					"ValidationId " + request.getValidationId() + " is already replayed using replayId "
							+ originalValidationRequest.getReplayData().getValidationId());
			return response;
		}

		// 4. check if there is a comparison validation id exist (if applicable)
		TestValidationData validationComparisonData = null;
		if (originalValidationRequest.getRunData().getComparisonRunData() != null) {
			validationComparisonData = testValidationDao.getLatestValidationByRunIdAndUniqueKey(
					originalValidationRequest.getRunData().getComparisonRunData().getRunId(),
					originalValidationRequest.getUniqueKey());
			if (validationComparisonData == null) {
				response.setErrorMessage("No active comparison validation id found for comparison runId: "
						+ originalValidationRequest.getRunData().getComparisonRunData().getRunId() + " and uniqueKey:"
						+ originalValidationRequest.getUniqueKey());
				return response;
			}
		}

		// 5. create new validation entry for replay in database table
		TestValidationData replayValidationRequest = testValidationDao
				.createTestValidation(prepareReplayRequest(originalValidationRequest, validationComparisonData));
		response.setData(replayValidationRequest);
		if (replayValidationRequest == null) {
			response.setErrorMessage("Unable to create replay entry in database at this time. Please try again later.");
			return response;
		}

		// 6. update existing entry with new replay id
		testValidationDao.updateReplayId(originalValidationRequest.getValidationId(),
				replayValidationRequest.getValidationId());

		// 7. enqueue request for execution
		boolean accepted = queueService.publishToQueue(replayValidationRequest);

		// 8. if queue is exhausted then delete the entry
		if (!accepted) {
			// delete an entry
			testValidationDao.deleteById(replayValidationRequest.getValidationId());
			// and return error
			response.setErrorMessage("Couldn't accept a new request due to server load. Queue is exhausted");
			response.setData(null);
		}

		return response;
	}

	private ValidatorResponse validateRequest(ReplayTestValidationRequest request) {
		ValidatorResponse response = new ValidatorResponse();

		if (request.getValidationId() == null || request.getValidationId() == 0) {
			response.addError("validationId is mandatory.");
		}

		return response;
	}

	private TestValidationData prepareReplayRequest(TestValidationData originalValidationRequest,
			TestValidationData validationComparisonData) {
		TestValidationData replayRequest = new TestValidationData();
		replayRequest.setValidationStatus(ValidationStatus.UNKNOWN);
		replayRequest.setRunData(originalValidationRequest.getRunData());
		replayRequest.setUniqueKey(originalValidationRequest.getUniqueKey());
		replayRequest.setValidationComparisonData(validationComparisonData);
		return replayRequest;
	}
}
