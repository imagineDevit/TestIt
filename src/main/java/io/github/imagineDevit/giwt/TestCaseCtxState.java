package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseState;

import java.util.Objects;

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
     * Returns the current value.
     *
     * @return the current value
     */
    protected T value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseCtxState<?> that = (TestCaseCtxState<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}