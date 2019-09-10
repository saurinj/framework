package com.sj.application.framework.step;

/**
 * 
 * All {@link Step} implementations which are needed to run in parallel should
 * implement this interface
 * 
 *
 */
public interface ParallelStep<T> extends Step<T> {

	// set default timeout to 1 minute
	long DEFAULT_SLA = 60000L;

	default long getSlaInMillis() {
		return DEFAULT_SLA;
	}
}
