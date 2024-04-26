package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseState;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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
     * Applies a function to the current value and returns a new TestCaseState with the result.
     *
     * @param mapper the function to apply to the current value
     * @return a new TestCaseState with the result of the function
     */
    protected TestCaseState<T> map(UnaryOperator<T> mapper) {
        return TestCaseState.of(mapper.apply(value));
    }

    /**
     * Applies a function to the current value and returns a new TestCaseResult with the result.
     *
     * @param mapper the function to apply to the current value
     * @return a new TestCaseResult with the result of the function
     */
    protected <R> TestCaseResult<R> mapToResult(Function<T, R> mapper) {
        return TestCaseResult.of(mapper.apply(value));
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
     * Returns an Optional containing the current value, or an empty Optional if the value is null.
     *
     * @return an Optional containing the current value
     */
    protected Optional<T> get() {
        return Optional.ofNullable(value);
    }
}