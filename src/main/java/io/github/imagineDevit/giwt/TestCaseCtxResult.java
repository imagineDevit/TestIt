package io.github.imagineDevit.giwt;

/**
 * A result of a test case with context
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseCtxResult<T> {
    private final T value;

    private TestCaseCtxResult(T value) {
        this.value = value;
    }

    static <T> TestCaseCtxResult<T> of(T value) {
        return new TestCaseCtxResult<>(value);
    }

    T value() {
        return value;
    }
}
