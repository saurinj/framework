package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sj.application.framework.step.StepEvent;
import com.sj.application.framework.step.StepEvent.StepEventType;
import com.sj.application.framework.step.StepStatus;
import com.sj.application.framework.validation.dal.TestValidationStepDAO;
import com.sj.application.framework.validation.model.TestValidationData;

@Component
public class TestValidationStepEventListener {
	
	@Inject
	private TestValidationStepDAO testValidationStepDao;

	@EventListener
	public void onApplicationEvent(StepEvent<TestValidationData> event) {
		// update step table with new status
		if (event.getStepEventType() == StepEventType.STARTED) {
			testValidationStepDao.insertValidationStep(event.getContext().getRequest().getValidationId(),
					event.getStep(), StepStatus.EXECUTING);
		} else if (event.getStepEventType() == StepEventType.COMPLETED) {
			testValidationStepDao.updateValidationStep(event.getStep());
		}
	}
	
}
