package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.ATestCaseResult;
import io.github.imagineDevit.giwt.core.utils.MutVal;
import io.github.imagineDevit.giwt.expectations.JExpectable;

import java.util.Objects;
import java.util.function.Function;

/**
 * A result of a test case
 *
 * @param <T> the result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseResult<T> extends ATestCaseResult<T> implements JExpectable<T> {

    private final MutVal<T> rValue = new MutVal<>();
    private final MutVal<Throwable> rError = new MutVal<>();

    /**
     * Private constructor for TestCaseResult.
     *
     * @param value the initial value of the test case result
     */
    private TestCaseResult(T value) {
        super(value);
    }

    /**
     * Private constructor for TestCaseResult.
     *
     * @param e the exception to be set as the value of the test case result
     */
    private TestCaseResult(Throwable e) {
        super(e);
    }

    /**
     * Factory method to create a new TestCaseResult with a given value.
     *
     * @param value the initial value of the test case result
     * @return a new TestCaseResult instance
     */
    protected static <T> TestCaseResult<T> of(T value) {
        return new TestCaseResult<>(value);
    }

    /**
     * Factory method to create a new TestCaseResult with a given exception.
     *
     * @param e the exception to be set as the value of the test case result
     * @return a new TestCaseResult instance
     */
    protected static <T> TestCaseResult<T> ofErr(Throwable e) {
        return new TestCaseResult<>(e);
    }

    /**
     * Factory method to create a new TestCaseResult with no initial value.
     *
     * @return a new TestCaseResult instance with no initial value
     */
    protected static <T> TestCaseResult<T> empty() {
        return new TestCaseResult<>((T) null);
    }

    /**
     * Applies a function to the current value and returns a new TestCaseResult with the result.
     *
     * @param mapper the function to apply to the current value
     * @return a new TestCaseResult with the result of the function
     * @throws IllegalStateException if the result is a failure
     */
    public <R> TestCaseResult<R> map(Function<T, R> mapper) {
        return value.<T>ok()
                .map(ResultValue.Ok::getValue)
                .map(v -> TestCaseResult.of(mapper.apply(v)))
                .orElseThrow(() -> new IllegalStateException("Result is Failure"));
    }

    @Override
    public T resultValue() {
        return rValue.getOr(
                () -> Objects.requireNonNull(value, "Result value is Null")
                        .<T>ok()
                        .orElseThrow(() -> new IllegalStateException("Result value is not an Ok"))
                        .getValue()
        );
    }

    @Override
    public Throwable resultError() {
        return rError.getOr(
                () -> Objects.requireNonNull(value, "Result value is Null")
                        .err()
                        .map(ATestCaseResult.ResultValue.Err::getError)
                        .orElseThrow(() -> new IllegalStateException("Result value is not a Failure"))
        );
    }
}