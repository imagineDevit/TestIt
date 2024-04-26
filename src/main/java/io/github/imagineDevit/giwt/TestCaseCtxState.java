package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseState;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A state of a test case with context
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseCtxState<T> extends ATestCaseState<T> {

    /**
     * Private constructor for TestCaseCtxState.
     *
     * @param value the initial value of the test case context state
     */
    private TestCaseCtxState(T value) {
        super(value);
    }

    /**
     * Factory method to create a new TestCaseCtxState with a given value.
     *
     * @param value the initial value of the test case context state
     * @return a new TestCaseCtxState instance
     */
    protected static <T> TestCaseCtxState<T> of(T value) {
        return new TestCaseCtxState<>(value);
    }

    /**
     * Factory method to create a new TestCaseCtxState with no initial value.
     *
     * @return a new TestCaseCtxState instance with no initial value
     */
    protected static <T> TestCaseCtxState<T> empty() {
        return new TestCaseCtxState<>(null);
    }

    /**
     * Applies a function to the current value and returns a new TestCaseCtxState with the result.
     *
     * @param mapper the function to apply to the current value
     * @return a new TestCaseCtxState with the result of the function
     */
    protected TestCaseCtxState<T> map(UnaryOperator<T> mapper) {
        return new TestCaseCtxState<>(mapper.apply(this.value()));
    }

    /**
     * Applies a function to the current value and returns a new TestCaseCtxResult with the result.
     *
     * @param mapper the function to apply to the current value
     * @return a new TestCaseCtxResult with the result of the function
     */
    protected <R> TestCaseCtxResult<R> toResult(Function<T, R> mapper) {
        try {
            return TestCaseCtxResult.of(mapper.apply(this.value()));
        } catch (Exception e) {
            return TestCaseCtxResult.ofErr(e);
        }
    }

    /**
     * Returns the current value.
     *
     * @return the current value
     */
    protected T value() {
        return value;
    }
}