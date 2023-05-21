package io.github.imagine.devit.TestIt;

import java.util.function.Function;

public class TestCaseState<T> {
    private final T value;

    private  TestCaseState(T value) {
        this.value = value;
    }

    public static <T> TestCaseState<T> of(T value){
        return new TestCaseState<>(value);
    }

    public static <T> TestCaseState<T> empty() {
        return new TestCaseState<>(null);
    }

    public <R> TestCaseState<R> map(Function<T,R> mapper){
        return TestCaseState.of(mapper.apply(value));
    }

    public <R> R mapAndGet(Function<T,R> mapper){
        return mapper.apply(value);
    }

}
