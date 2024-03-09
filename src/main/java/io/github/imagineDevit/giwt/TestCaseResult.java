package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.assertions.Assertable;
import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.function.Function;

/**
 * A result of a test case
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseResult<T> extends ATestCaseResult<T> implements Assertable<T> {

    private TestCaseResult(T value) {
        super(value);
    }

    private TestCaseResult(Exception e) {
        super(e);
    }

    protected static <T> TestCaseResult<T> of(T value) {
        return new TestCaseResult<>(value);
    }

    protected static <T> TestCaseResult<T> ofErr(Exception e) {
        return new TestCaseResult<>(e);
    }

    protected static <T> TestCaseResult<T> empty() {
        return new TestCaseResult<>((T) null);
    }

    public <R> TestCaseResult<R> map(Function<T, R> mapper) {
        return value.<T>ok()
                .map(ResultValue.Ok::getValue)
                .map(v -> TestCaseResult.of(mapper.apply(v)))
                .orElseThrow();
    }

    @Override
    public ResultValue value() {
        return this.value;
    }
}
