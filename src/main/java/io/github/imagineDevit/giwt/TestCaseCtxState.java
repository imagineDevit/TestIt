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

    private TestCaseCtxState(T value) {
        super(value);
    }

    protected static <T> TestCaseCtxState<T> of(T value) {
        return new TestCaseCtxState<>(value);
    }

    protected static <T> TestCaseCtxState<T> empty() {
        return new TestCaseCtxState<>(null);
    }

    protected TestCaseCtxState<T> map(UnaryOperator<T> mapper) {
        return new TestCaseCtxState<>(mapper.apply(this.value()));
    }

    protected <R> TestCaseCtxResult<R> toResult(Function<T, R> mapper) {
        try {
            return TestCaseCtxResult.of(mapper.apply(this.value()));
        } catch (Exception e) {
            return TestCaseCtxResult.ofErr(e);
        }
    }

    protected T value() {
        return value;
    }
}
