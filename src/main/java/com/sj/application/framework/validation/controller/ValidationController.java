package com.sj.application.framework.validation.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sj.application.framework.validation.model.CreateTestValidationRequest;
import com.sj.application.framework.validation.model.ReplayTestValidationRequest;
import com.sj.application.framework.validation.model.TestValidationResponse;
import com.sj.application.framework.validation.model.ValidationRunRequest;
import com.sj.application.framework.validation.model.ValidationRunResponse;
import com.sj.application.framework.validation.service.ValidationService;

/**
 * 
 * Single entry point for all validation related REST apis
 *
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {

	@Inject
	private ValidationService service;
	
	/**
	 * This method creates new run entry in database and returns runId back to client.
	 * <p>
	 * Client should call subsequent testcase validation using this runId, so that validation framework knows that all testcases are part of same run
	 * <p>
	 * This helps in doing baseline-regression comparison in that regression testrun can pass runId of baseline for comparison
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/run", method = RequestMethod.POST)
	public @ResponseBody ValidationRunResponse createRunEntry(@RequestBody ValidationRunRequest request) {
		return service.createRunEntry(request);
	}
	
	/**
	 * This method retrieves status of an existing run
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/run/{runId}", method = RequestMethod.GET)
	public @ResponseBody ValidationRunResponse getRun(@PathVariable("runId") Long runId) {
		return service.getRun(runId);
	}
	
	/**
	 * This method creates new validation entry in database.
	 * <p>
	 * <li>It performs required validation in real-time</li>
	 * <li>If all validations are successful, it queues the validation request, which is performed in async manner</li>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/testvalidation", method = RequestMethod.POST)
	public @ResponseBody TestValidationResponse createTestValidation(@RequestBody CreateTestValidationRequest request) {
		return service.createTestValidation(request);
	}
	
	/**
	 * This method replays an existing validation request.
	 * <p>
	 * <li>It performs required validation in real-time</li>
	 * <li>If all validations are successful, it queues the validation request, which is executed in async manner</li>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/testvalidation/replay", method = RequestMethod.POST)
	public @ResponseBody TestValidationResponse replayTestValidation(@RequestBody ReplayTestValidationRequest request) {
		return service.replayTestValidation(request);
	}
	
	/**
	 * This method retrieves status of an existing validation request
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/testvalidation/{validationId}", method = RequestMethod.GET)
	public @ResponseBody TestValidationResponse getTestValidationStatus(@PathVariable("validationId") Long validationId) {
		return service.getTestValidationStatus(validationId);
	}
	
}
