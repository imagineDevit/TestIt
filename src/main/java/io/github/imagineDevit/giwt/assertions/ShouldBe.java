package io.github.imagineDevit.giwt.assertions;

import io.github.imagineDevit.giwt.core.ATestCaseResult;
import io.github.imagineDevit.giwt.core.utils.Utils;

import java.util.Collection;
import java.util.Objects;

/**
 *
 * @param result the result value
 * @param <T> type of the result value
 * @see ATestCaseResult.ResultValue
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public record ShouldBe<T>(ATestCaseResult.ResultValue.Ok<T> result) {

    public void null_() {
        if (result.getValue() != null) {
            throw new AssertionError("Expected null value but got " + result.getValue());
        }
    }

    public ShouldBe<T> notNull() {
        if (result.getValue() == null) {
            throw new AssertionError("Expected not null value but got <null>");
        }

        return this;
    }

    public ShouldBe<T> equalTo(T expected) {
        if (!Objects.equals(result.getValue(), expected)) {
            throw new AssertionError("Expected value to be <" + expected + "> but got <" + result.getValue() + ">");
        }

        return this;
    }

    public ShouldBe<T> notEqualTo(T expected) {
        if (Objects.equals(result.getValue(), expected)) {
            throw new AssertionError("Expected value to be different from <" + expected + "> but got <" + result.getValue() + ">");
        }

        return this;
    }

    public ShouldBe<T> between(T min, T max) {

        var c = Utils.asComparableOrThrow(
                result.getValue(),
                () -> new AssertionError("Value is not comparable")
        );

        if (c.compareTo(min) < 0 || c.compareTo(max) > 0) {
            throw new AssertionError("Expected value to be between <" + min + "> and <" + max + "> but got <" + c + ">");
        }

        return this;
    }

    public ShouldBe<T> greaterThan(T min) {
        var c = Utils.asComparableOrThrow(
                result.getValue(),
                () -> new AssertionError("Value is not comparable")
        );
        if (c.compareTo(min) <= 0) {
            throw new AssertionError("Expected value to be greater than <" + min + "> but got <" + c + ">");
        }

        return this;
    }

    public ShouldBe<T> lesserThan(T max) {

        var c = Utils.asComparableOrThrow(
                result.getValue(),
                () -> new AssertionError("Value is not comparable")
        );
        if (c.compareTo(max) >= 0) {
            throw new AssertionError("Expected value to be lesser than <" + max + "> but got <" + c + ">");
        }

        return this;
    }

    public ShouldBe<T> oneOf(Collection<T> values) {
        if (!values.contains(result.getValue())) {
            throw new AssertionError("Expected value to be one of <" + values + "> but got <" + result.getValue() + ">");
        }

        return this;
    }

}