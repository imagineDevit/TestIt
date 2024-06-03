package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseState;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A state of a test case
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseState<T> extends ATestCaseState<T> {

    /**
     * Constructor for TestCaseState.
     *
     * @param value the initial value of the test case state
     */
    protected TestCaseState(T value) {
        super(value);
    }

    /**
     * Factory method to create a new TestCaseState with a given value.
     *
     * @param value the initial value of the test case state
     * @return a new TestCaseState instance
     */
    protected static <T> TestCaseState<T> of(T value) {
        return new TestCaseState<>(value);
    }

    /**
     * Factory method to create a new TestCaseState with no initial value.
     *
     * @return a new TestCaseState instance with no initial value
     */
    protected static <T> TestCaseState<T> empty() {
        return new TestCaseState<>(null);
    }

    /**
     * Consumes the current value using a provided Consumer.
     *
     * @param consumer the Consumer to apply to the current value
     */
    @Override
    protected void consumeValue(Consumer<T> consumer) {
        super.consumeValue(consumer);
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
        TestCaseState<?> that = (TestCaseState<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}