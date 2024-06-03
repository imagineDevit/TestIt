package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.Objects;

/**
 * A result of a test case with context
 *
 * @param <R> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseCtxResult<R> extends ATestCaseResult<R> {

    /**
     * Private constructor that accepts a value
     *
     * @param value the value to be wrapped in the TestCaseCtxResult
     */
    private TestCaseCtxResult(R value) {
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
    protected static <R> TestCaseCtxResult<R> of(R value) {
        return new TestCaseCtxResult<>(value);
    }

    /**
     * Factory method to create a new TestCaseCtxResult with a null value
     *
     * @return a new TestCaseCtxResult instance with a null value
     */
    protected static <R> TestCaseCtxResult<R> empty() {
        return new TestCaseCtxResult<>((R) null);
    }

    /**
     * Factory method to create a new TestCaseCtxResult with a given Exception
     *
     * @param e the Exception to be wrapped in the TestCaseCtxResult
     * @return a new TestCaseCtxResult instance with the given Exception
     */
    protected static <T> TestCaseCtxResult<T> ofErr(Exception e) {
        return new TestCaseCtxResult<>(Objects.requireNonNull(e));
    }

    /**
     * Method to get the result of the TestCaseCtxResult
     *
     * @return a TestCaseResult instance with the value or exception of this TestCaseCtxResult
     */
    protected TestCaseResult<R> result() {
        return Objects.requireNonNull(value, "Result value is Null").<R>ok()
                .map(v -> TestCaseResult.of(v.getValue()))
                .orElseGet(() -> TestCaseResult.ofErr(
                                value.err()
                                        .map(ResultValue.Err::getError)
                                        .orElseThrow(() -> new IllegalStateException("Unexpected error occurred while mapping result to TestCaseResult"))
                        )
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseCtxResult<?> that = (TestCaseCtxResult<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}