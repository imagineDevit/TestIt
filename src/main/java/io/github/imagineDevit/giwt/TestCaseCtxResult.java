package io.github.imagineDevit.giwt;

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
