package io.github.imagineDevit.giwt.assertions;


import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.Arrays;
import java.util.Objects;

/**
 * Interface for the assertion methods.
 *
 * @param <T> type of the result value
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 * @see ATestCaseResult.ResultValue
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public interface Assertable<T> {

    default void shouldFail(ShouldFails... expectations) {
        Exception exception = Objects.requireNonNull(value(), "Result value is Null")
                .err()
                .map(ATestCaseResult.ResultValue.Err::getError)
                .orElseThrow(() -> new AssertionError("Result value is Ok"));

        Arrays.asList(expectations)
                .forEach(f -> f.verify(exception));
    }

    default void shouldBe(ShouldBes... expectations) {
        T value = Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ATestCaseResult.ResultValue.Ok::getValue)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));

        Arrays.asList(expectations)
                .forEach(b -> b.verify(value));
    }

    default void shouldHave(ShouldHaves... expectations) {
        T value = Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ATestCaseResult.ResultValue.Ok::getValue)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));

        Arrays.asList(expectations)
                .forEach(h -> h.verify(value));
    }

    default void shouldMatch(ShouldMatchs... expectations) {
        T value = Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ATestCaseResult.ResultValue.Ok::getValue)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));

        Arrays.asList(expectations).forEach(m -> m.verify(value));
    }

    ATestCaseResult.ResultValue value();
}
