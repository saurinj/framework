package com.sj.application.framework.validation.service;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.model.TestValidationData;

/**
 * 
 * This class consumes the validation request from work-queue and calls
 * StepExecutorService to execute validation steps
 * <p>
 * It consumes queue every 5 seconds for incoming validation requests
 *
 */
@Service
public class QueueConsumerService {

	@Inject
	private QueueService queueService;

	@Inject
	private TestValidationExecutionService stepExecutorService;

	@Scheduled(fixedRate = 20000)
	public void consumeQueue() {
		while (!queueService.isQueueEmpty()) {
			TestValidationData request = queueService.consumeFromQueue();
			if (request == null) {
				return;
			}
			stepExecutorService.execute(request);
		}
	}

}
