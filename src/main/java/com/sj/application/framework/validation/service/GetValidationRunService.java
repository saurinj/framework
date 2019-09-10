package com.sj.application.framework.validation.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.dal.TestValidationDAO;
import com.sj.application.framework.validation.dal.ValidationRunDAO;
import com.sj.application.framework.validation.model.TestValidationData;
import com.sj.application.framework.validation.model.ValidationRunData;
import com.sj.application.framework.validation.model.ValidationRunResponse;
import com.sj.application.framework.validation.model.ValidatorResponse;

@Service
public class GetValidationRunService {
	
	@Inject
	private ValidationRunDAO validationRunDAO;

	@Inject
	private TestValidationDAO testValidationDao;

	public ValidationRunResponse getRun(Long runId) {
		ValidationRunResponse response = new ValidationRunResponse();
		// 1. validate request
		ValidatorResponse validatorResponse = validateRequest(runId);
		if (!validatorResponse.isValid()) {
			response.setErrorMessage(validatorResponse.getErrorMessage());
			return response;
		}

		// 2. get run data
		ValidationRunData data = validationRunDAO.getRunData(runId);
		response.setData(data);
		if (data == null) {
			response.setErrorMessage("Invalid runId");
			return response;
		}
		
		// 3. get test validation data
		List<TestValidationData> testValidationDataList = testValidationDao.getValidationsByRunId(runId);
		response.setTestValidationDataList(testValidationDataList);

		return response;
	}

	private ValidatorResponse validateRequest(Long runId) {
		ValidatorResponse response = new ValidatorResponse();

		if (runId == null || runId == 0) {
			response.addError("runId is mandatory.");
		}

		return response;
	}
}
