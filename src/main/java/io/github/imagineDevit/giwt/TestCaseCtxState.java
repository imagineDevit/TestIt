package io.github.imagineDevit.giwt;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A state of a test case with context
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseCtxState<T> {
    private final T value;

    private TestCaseCtxState(T value) {
        this.value = value;
    }

    public static <T> TestCaseCtxState<T> of(T value) {
        return new TestCaseCtxState<>(value);
    }

    protected T getValue() {
        return value;
    }

    public TestCaseCtxState<T> map(UnaryOperator<T> mapper) {
        return TestCaseCtxState.of(mapper.apply(value));
    }

    public <R> TestCaseCtxResult<R> toResult(Function<T, R> mapper) {
        return TestCaseCtxResult.of(mapper.apply(value));
    }

}
