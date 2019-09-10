package com.sj.application.framework.step;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Parent class for all retriable operations. This class has the sole responsibility of performing retry.
 * <p>
 * It keeps retrying until either
 * <li>We got desired output</li>
 * <li>or we exceeded retry time limit</li>
 *
 * @param <I>
 * @param <O>
 */
@Component
 public class RetrierFunction<I, O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrierFunction.class);

    private static final long MAX_WAIT_IN_MS = 180 * 1000;
    private static final long REPEAT_INTERVAL_IN_MS = 15 * 1000;

    public O execute(Function<I, O> executeFunction, BiFunction<I, O, Boolean> isDesiredOutput, I input) {
        return execute(executeFunction, isDesiredOutput, input, MAX_WAIT_IN_MS, REPEAT_INTERVAL_IN_MS);
    }

    public O execute(Function<I, O> executeFunction, BiFunction<I, O, Boolean> isDesiredOutput,
            BiFunction<I, O, O> onSuccess, BiFunction<I, O, O> onFailure, I input) {
        return execute(executeFunction, isDesiredOutput, onSuccess, onFailure, input, MAX_WAIT_IN_MS,
                REPEAT_INTERVAL_IN_MS);
    }

    public O execute(Function<I, O> executeFunction, BiFunction<I, O, Boolean> isDesiredOutput, I input,
            long maxWaitInMs, long repeatIntervalInMs) {
        BiFunction<I, O, O> onSuccess = (i, o) -> o;
        BiFunction<I, O, O> onFailure = (i, o) -> o;
        return execute(executeFunction, isDesiredOutput, onSuccess, onFailure, input, maxWaitInMs, repeatIntervalInMs);
    }

    public O execute(Function<I, O> executeFunction, BiFunction<I, O, Boolean> isDesiredOutputFunction,
            BiFunction<I, O, O> onSuccessFunction, BiFunction<I, O, O> onFailureFunction, I input, long maxWaitInMs,
            long repeatIntervalInMs) {
        long startTime = System.currentTimeMillis();
        O output = null;
        boolean hasTimeToRetry = true;
        do {
            // execute task
            output = executeFunction.apply(input);

            // check if got desired output. If not, continue.
            if (isDesiredOutputFunction.apply(input, output)) {
                return onSuccessFunction.apply(input, output);
            }

            // wait for pre-defined time interval
            try {
                Thread.sleep(repeatIntervalInMs);
            } catch (InterruptedException iexpt) {
                return onFailureFunction.apply(input, output);
            }

            // check if out-of-time
            hasTimeToRetry = hasNotExpired(startTime, maxWaitInMs);
            if (hasTimeToRetry) {
                LOGGER.warn("Retrying task: {}", executeFunction.getClass());
            }
        } while (hasTimeToRetry);

        return onFailureFunction.apply(input, output);
    }

    private boolean hasNotExpired(long startTime, long maxWaitInMs) {
        return (System.currentTimeMillis() - startTime) <= maxWaitInMs;
    }
}
