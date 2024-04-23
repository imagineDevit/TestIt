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

    private TestCaseCtxResult(T value) {
        super(value);
    }

    private TestCaseCtxResult(Exception e) {
        super(e);
    }

    protected static <T> TestCaseCtxResult<T> of(T value) {
        return new TestCaseCtxResult<>(value);
    }

    protected static <T> TestCaseCtxResult<T> empty() {
        return new TestCaseCtxResult<>(null);
    }

    protected static <T> TestCaseCtxResult<T> ofErr(Exception e) {
        return new TestCaseCtxResult<>(e);
    }

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
