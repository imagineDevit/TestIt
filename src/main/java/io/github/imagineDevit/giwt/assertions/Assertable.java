package io.github.imagineDevit.giwt.assertions;


import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Interface for the assertion methods.
 * @param <T> type of the result value
 * @see ATestCaseResult.ResultValue
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings("unused")
public interface Assertable<T> {

    default ShouldFail shouldFail() {
        return Objects.requireNonNull(value(), "Result value is Null")
                .err()
                .map(ShouldFail::new)
                .orElseThrow(() -> new AssertionError("Result value is Ok"));
    }

    default ShouldBe<T> shouldBe() {
        return Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ShouldBe::new)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }

    default ShouldHave<T> shouldHave() {
        return Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ShouldHave::new)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }

    default ShouldMatch<T> shouldMatch() {
        return Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ShouldMatch::new)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }

    default void shouldMatch(Predicate<T> predicate) {
        Objects.requireNonNull(value(), "Result value is Null")
                .<T>ok()
                .map(ShouldMatch::new)
                .map(m -> m.one(ShouldMatch.matching("predicate", predicate)))
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }

    ATestCaseResult.ResultValue value();
}
