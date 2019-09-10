package com.sj.application.framework.validation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidatorResponse {

	public List<String> errors = new ArrayList<>();
	
	public void addError(String message) {
		this.errors.add(message);
	}
	
	public boolean isValid() {
		return this.errors.isEmpty();
	}
	
	public String getErrorMessage() {
		return this.errors.stream().collect(Collectors.joining(","));
	}
	
}
