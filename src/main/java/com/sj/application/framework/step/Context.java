package com.sj.application.framework.step;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context<T> {
	
	private T request;
	private Map<String, Step<T>> stepMap = new ConcurrentHashMap<>();
	
	public Context(T request) {
		this.request = request;
	}
	
	public T getRequest() {
		return request;
	}
	
	public void addStep(Step<T> step) {
		this.stepMap.put(step.getStepData().getStepName(), step);
	}
	
	public Step<T> getStep(String name) {
		return stepMap.get(name);
	}

}
