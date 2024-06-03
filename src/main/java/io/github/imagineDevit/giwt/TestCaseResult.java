package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.ATestCaseResult;
import io.github.imagineDevit.giwt.core.utils.MutVal;
import io.github.imagineDevit.giwt.expectations.JExpectable;

import java.util.Objects;
import java.util.function.Function;

/**
 * A result of a test case
 *
 * @param <R> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseResult<R> extends ATestCaseResult<R> implements JExpectable<R> {

    private final MutVal<R> rValue = new MutVal<>();
    private final MutVal<Throwable> rError = new MutVal<>();

    /**
     * Private constructor for TestCaseResult.
     *
     * @param value the initial value of the test case result
     */
    private TestCaseResult(R value) {
        super(value);
    }

    /**
     * Private constructor for TestCaseResult.
     *
     * @param e the exception to be set as the value of the test case result
     */
    private TestCaseResult(Throwable e) {
        super(Objects.requireNonNull(e));
    }

    /**
     * Factory method to create a new TestCaseResult with a given value.
     *
     * @param value the initial value of the test case result
     * @return a new TestCaseResult instance
     */
    protected static <R> TestCaseResult<R> of(R value) {
        return new TestCaseResult<>(value);
    }

    /**
     * Factory method to create a new TestCaseResult with a given exception.
     *
     * @param e the exception to be set as the value of the test case result
     * @return a new TestCaseResult instance
     */
    protected static <R> TestCaseResult<R> ofErr(Throwable e) {
        return new TestCaseResult<>(Objects.requireNonNull(e));
    }

    /**
     * Factory method to create a new TestCaseResult with no initial value.
     *
     * @return a new TestCaseResult instance with no initial value
     */
    protected static <R> TestCaseResult<R> empty() {
        return new TestCaseResult<>((R) null);
    }

    /**
     * Applies a function to the current value and returns a new TestCaseResult with the result.
     *
     * @param mapper the function to apply to the current value
     * @return a new TestCaseResult with the result of the function
     * @throws IllegalStateException if the result is a failure
     */
    public <S> TestCaseResult<S> map(Function<R, S> mapper) {
        return value.<R>ok()
                .map(v -> TestCaseResult.of(mapper.apply(v.getValue())))
                .orElseThrow(() -> new IllegalStateException("Result is Failure"));
    }

    @Override
    public R resultValue() {
        return rValue.getOr(() -> Objects.requireNonNull(value, "Result value is Null")
                .<R>ok()
                .orElseThrow(() -> new IllegalStateException("Result is Failure"))
                .getValue()
        );
    }

    @Override
    public Throwable resultError() {
        return rError.getOr(() -> Objects.requireNonNull(value, "Result value is Null")
                .err()
                .orElseThrow(() -> new IllegalStateException("Result is Success"))
                .getError()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseResult<?> that = (TestCaseResult<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}