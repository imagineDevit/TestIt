package io.github.imagineDevit.giwt.assertions;

import io.github.imagineDevit.giwt.core.ATestCaseResult;

/**
 *
 * @param result the result value
 * @see ATestCaseResult.ResultValue
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public record ShouldFail(ATestCaseResult.ResultValue.Err<?> result) {

    public ShouldFail withErrorOfType(Class<? extends Exception> errorClass) {
        if (!errorClass.isInstance(result.getError())) {
            throw new AssertionError("Expected error to be of type <" + errorClass.getName() + "> but got <" + result.getError().getClass().getName() + ">");
        }
        return this;
    }

    public ShouldFail withMessage(String message) {
        if (!result.getError().getMessage().equals(message)) {
            throw new AssertionError("Expected error message to be <" + message + "> but got <" + result.getError().getMessage() + ">");
        }
        return this;
    }

}
