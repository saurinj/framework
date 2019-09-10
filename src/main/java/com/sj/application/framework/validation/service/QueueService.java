package com.sj.application.framework.validation.service;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import com.sj.application.framework.validation.model.TestValidationData;

/**
 * 
 * This class maintains a Queue for incoming validation requests.
 * <p>
 * This is necessary as validation request can take few minutes to complete and client thread doesn't/shouldn't wait for that long.
 * <p>
 * Instead, client gets a handle (validationId) back, which they can use to poll the status of the validation
 *
 */
@Service
public class QueueService {
	
	private Queue<TestValidationData> queue = new LinkedBlockingQueue<>();

	public boolean publishToQueue(TestValidationData validationRequest) {
		return queue.offer(validationRequest);
	}
	
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}
	
	public TestValidationData consumeFromQueue() {
		return queue.poll();
	}
}
