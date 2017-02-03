package com.jmeter.websocket.plugin.elements.helpers;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.apache.jmeter.assertions.AssertionResult;

import static com.google.common.base.Optional.fromNullable;
import static com.jmeter.websocket.plugin.elements.helpers.ExpectationResult.failure;

public final class ExpectationUtils {
    public static final String UNKNOWN = "Result is unknown.";

    private ExpectationUtils() {
    }

    public static Expectation expectationFrom(ExpectationResult result) {
        return new ExpectationResultContainer(result);
    }

    public static Expectation failedExpectationWithReason(String reason) {
        return expectationFrom(failure(reason));
    }

    public static AssertionResult assertionResultFrom(final String name, ExpectationResult expectationResult) {
        return fromNullable(expectationResult)
                .transform(new Function<ExpectationResult, AssertionResult>() {
                    @Override
                    public AssertionResult apply(ExpectationResult expectationResult) {
                        AssertionResult assertionResult = new AssertionResult(name);
                        assertionResult.setFailure(!expectationResult.isSuccess());
                        assertionResult.setFailureMessage(expectationResult.getReason());
                        return assertionResult;
                    }
                })
                .or(new Supplier<AssertionResult>() {
                    @Override
                    public AssertionResult get() {
                        AssertionResult assertionResult = new AssertionResult(name);
                        assertionResult.setError(true);
                        assertionResult.setFailureMessage(UNKNOWN);
                        return assertionResult;
                    }
                });
    }

    public static class ExpectationResultContainer implements Expectation {
        private final ExpectationResult result;

        private ExpectationResultContainer(ExpectationResult result) {
            this.result = result;
        }

        @Override
        public ExpectationResult getResult(long timeout) {
            return result;
        }
    }
}