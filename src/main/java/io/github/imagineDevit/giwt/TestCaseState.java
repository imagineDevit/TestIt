package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCaseState;

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

    protected TestCaseState(T value) {
        super(value);
    }

    public static <T> TestCaseState<T> of(T value) {
        return new TestCaseState<>(value);
    }

    public static <T> TestCaseState<T> empty() {
        return new TestCaseState<>(null);
    }

    public TestCaseState<T> map(UnaryOperator<T> mapper) {
        return TestCaseState.of(mapper.apply(value));
    }

    protected <R> TestCaseResult<R> mapToResult(Function<T, R> mapper) {
        return TestCaseResult.of(mapper.apply(value));
    }
}
