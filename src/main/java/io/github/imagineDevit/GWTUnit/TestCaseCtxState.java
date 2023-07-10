package io.github.imagineDevit.GWTUnit;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TestCaseCtxState<T> {
    private final T value;

    private TestCaseCtxState(T value) {
        this.value = value;
    }

    public static <T> TestCaseCtxState<T> of(T value){
        return new TestCaseCtxState<>(value);
    }

    public TestCaseCtxState<T> map(UnaryOperator<T> mapper){
        return TestCaseCtxState.of(mapper.apply(value));
    }

    public <R> TestCaseCtxResult<R> toResult(Function<T,R> mapper){
        return TestCaseCtxResult.of(mapper.apply(value));
    }

}
