package io.github.imagineDevit.GWTUnit;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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

    public TestCaseState<T> map(UnaryOperator<T> mapper){
        return TestCaseState.of(mapper.apply(value));
    }

    protected <R> TestCaseResult<R> mapToResult(Function<T,R> mapper){
        return TestCaseResult.of(mapper.apply(value));
    }

    protected void consumeValue(Consumer<T> consumer){
        consumer.accept(value);
    }

}
