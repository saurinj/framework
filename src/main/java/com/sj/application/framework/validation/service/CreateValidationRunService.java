package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.dal.ValidationRunDAO;
import com.sj.application.framework.validation.model.ValidationRunData;
import com.sj.application.framework.validation.model.ValidationRunRequest;
import com.sj.application.framework.validation.model.ValidationRunResponse;
import com.sj.application.framework.validation.model.ValidatorResponse;

@Service
public class CreateValidationRunService {
	
	@Inject
	private ValidationRunDAO validationRunDao;

	/**
	 * This method creates new run entry in database and returns runId back to
	 * client.
	 * <p>
	 * Client should call subsequent testcase validation using this runId, so that
	 * validation framework knows that all testcases are part of same run
	 * <p>
	 * This helps in doing baseline-regression comparison in that regression testrun
	 * can pass runId of baseline for comparison
	 * 
	 * @param request
	 * @return ValidationRunResponse
	 */
	public ValidationRunResponse createRunEntry(ValidationRunRequest request) {
		ValidationRunResponse response = new ValidationRunResponse();
		// 1. perform request validation
		ValidatorResponse validatorResponse = validateRequest(request);
		if (!validatorResponse.isValid()) {
			response.setErrorMessage(validatorResponse.getErrorMessage());
			return response;
		}
		
		// 2. create run in database
		try {
			ValidationRunData data = validationRunDao.createRun(prepareRunRequest(request));
			response.setData(data);
		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
		}
		return response;
	}
	
	private ValidatorResponse validateRequest(ValidationRunRequest request) {
		ValidatorResponse response = new ValidatorResponse();
		if (request.getValidationType() == null) {
			response.addError("validationType is mandatory.");
		}
		
		if (StringUtils.isBlank(request.getStartedBy())) {
			response.addError("startedBy is mandatory.");
		}

		// comparisonRunId is not mandatory. But if specified, should be non-zero
		if (request.getComparisonRunId() != null && request.getComparisonRunId() == 0) {
			response.addError("comparisonRunId should be non-zero.");
		}
		
		return response;
	}

	private ValidationRunData prepareRunRequest(ValidationRunRequest request) {
		ValidationRunData data = new ValidationRunData();
		data.setStartedBy(request.getStartedBy());
		data.setValidationType(request.getValidationType());
		if (request.getComparisonRunId() != null) {
			ValidationRunData comparisonRunData = new ValidationRunData();
			comparisonRunData.setRunId(request.getComparisonRunId());
			data.setComparisonRunData(comparisonRunData);
		}
		return data;
	}
}
