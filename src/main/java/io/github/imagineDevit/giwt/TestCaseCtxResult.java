package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.Objects;

/**
 * A result of a test case with context
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseCtxResult<T> extends ATestCaseResult<T> {

    /**
     * Private constructor that accepts a value
     *
     * @param value the value to be wrapped in the TestCaseCtxResult
     */
    private TestCaseCtxResult(T value) {
        super(value);
    }

    /**
     * Private constructor that accepts an Exception
     *
     * @param e the Exception to be wrapped in the TestCaseCtxResult
     */
    private TestCaseCtxResult(Exception e) {
        super(e);
    }

    /**
     * Factory method to create a new TestCaseCtxResult with a given value
     *
     * @param value the value to be wrapped in the TestCaseCtxResult
     * @return a new TestCaseCtxResult instance with the given value
     */
    protected static <T> TestCaseCtxResult<T> of(T value) {
        return new TestCaseCtxResult<>(value);
    }

    /**
     * Factory method to create a new TestCaseCtxResult with a null value
     *
     * @return a new TestCaseCtxResult instance with a null value
     */
    protected static <T> TestCaseCtxResult<T> empty() {
        return new TestCaseCtxResult<>(null);
    }

    /**
     * Factory method to create a new TestCaseCtxResult with a given Exception
     *
     * @param e the Exception to be wrapped in the TestCaseCtxResult
     * @return a new TestCaseCtxResult instance with the given Exception
     */
    protected static <T> TestCaseCtxResult<T> ofErr(Exception e) {
        return new TestCaseCtxResult<>(e);
    }

    /**
     * Method to get the result of the TestCaseCtxResult
     *
     * @return a TestCaseResult instance with the value or Exception of this TestCaseCtxResult
     */
    protected TestCaseResult<T> result() {
        return Objects.requireNonNull(value, "Result value is Null")
                .<T>ok()
                .map(ResultValue.Ok::getValue)
                .map(TestCaseResult::of)
                .orElseGet(() ->
                        TestCaseResult.ofErr(
                                value.err().map(ResultValue.Err::getError).orElseThrow()
                        )
                );
    }

}