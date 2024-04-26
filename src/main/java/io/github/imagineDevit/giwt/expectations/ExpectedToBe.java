package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.utils.Utils;

import java.util.Collection;

/**
 * This interface provides a set of static methods to create different types of expectations.
 * Each expectation is a record that implements the ExpectedToBe interface and overrides the verify method.
 * The verify method is used to check if a value meets the expectation.
 * If the value does not meet the expectation, an AssertionError is thrown.
 *
 * @param <T> the type of the value to be checked
 */
@SuppressWarnings({"unused"})
public sealed interface ExpectedToBe<T> extends Expectation.OnValue<T> {

    /**
     * Creates an expectation that a value should be null.
     *
     * @param <T> the type of the value to be checked
     * @return a Null expectation
     */
    static <T> Null<T> null_() {
        return new Null<>();
    }

    /**
     * Creates an expectation that a value should not be null.
     *
     * @param <T> the type of the value to be checked
     * @return a NotNull expectation
     */
    static <T> NotNull<T> notNull() {
        return new NotNull<>();
    }

    /**
     * Creates an expectation that a value should be equal to the expected value.
     *
     * @param <T>      the type of the value to be checked
     * @param expected the expected value
     * @return an EqualTo expectation
     */
    static <T> EqualTo<T> equalTo(T expected) {
        return new EqualTo<>(expected);
    }

    /**
     * Creates an expectation that a value should not be equal to the expected value.
     *
     * @param <T>      the type of the value to be checked
     * @param expected the expected value
     * @return a NotEqualTo expectation
     */
    static <T> NotEqualTo<T> notEqualTo(T expected) {
        return new NotEqualTo<>(expected);
    }

    /**
     * Creates an expectation that a value should be between the min and max values.
     *
     * @param <T> the type of the value to be checked
     * @param min the minimum value
     * @param max the maximum value
     * @return a Between expectation
     */
    static <T> Between<T> between(T min, T max) {
        return new Between<>(min, max);
    }

    /**
     * Creates an expectation that a value should be greater than the min value.
     *
     * @param <T> the type of the value to be checked
     * @param min the minimum value
     * @return a GreaterThan expectation
     */
    static <T> GreaterThan<T> greaterThan(T min) {
        return new GreaterThan<>(min);
    }

    /**
     * Creates an expectation that a value should be lesser than the max value.
     *
     * @param <T> the type of the value to be checked
     * @param max the maximum value
     * @return a LesserThan expectation
     */
    static <T> LesserThan<T> lesserThan(T max) {
        return new LesserThan<>(max);
    }

    /**
     * Creates an expectation that a value should be one of the values in the collection.
     *
     * @param <T>    the type of the value to be checked
     * @param values the collection of values
     * @return a OneOf expectation
     */
    static <T> OneOf<T> oneOf(Collection<T> values) {
        return new OneOf<>(values);
    }

    /**
     * This record represents an expectation that a value should be null.
     *
     * @param <T> the type of the value to be checked
     */
    record Null<T>() implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            if (value != null) {
                throw new AssertionError("Expected null value but got " + value);
            }
        }
    }

    /**
     * This record represents an expectation that a value should not be null.
     *
     * @param <T> the type of the value to be checked
     */
    record NotNull<T>() implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            if (value == null) {
                throw new AssertionError("Expected not null value but got <null>");
            }
        }
    }

    /**
     * This record represents an expectation that a value should be equal to the expected value.
     *
     * @param <T>      the type of the value to be checked
     * @param expected the expected value
     */
    record EqualTo<T>(T expected) implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            if (!value.equals(expected)) {
                throw new AssertionError("Expected value to be <" + expected + "> but got <" + value + ">");
            }
        }
    }

    /**
     * This record represents an expectation that a value should not be equal to the expected value.
     *
     * @param <T>      the type of the value to be checked
     * @param expected the expected value
     */
    record NotEqualTo<T>(T expected) implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            if (value.equals(expected)) {
                throw new AssertionError("Expected value to be different from <" + expected + "> but got <" + value + ">");
            }
        }
    }

    /**
     * This record represents an expectation that a value should be between the min and max values.
     *
     * @param <T> the type of the value to be checked
     * @param min the minimum value
     * @param max the maximum value
     */
    record Between<T>(T min, T max) implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            var c = Utils.asComparableOrThrow(
                    value,
                    () -> new AssertionError("Value is not comparable")
            );

            if (c.compareTo(min) < 0 || c.compareTo(max) > 0) {
                throw new AssertionError("Expected value to be between <" + min + "> and <" + max + "> but got <" + value + ">");
            }
        }
    }

    /**
     * This record represents an expectation that a value should be greater than the min value.
     *
     * @param <T> the type of the value to be checked
     * @param min the minimum value
     */
    record GreaterThan<T>(T min) implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            var c = Utils.asComparableOrThrow(
                    value,
                    () -> new AssertionError("Value is not comparable")
            );

            if (c.compareTo(min) <= 0) {
                throw new AssertionError("Expected value to be greater than <" + min + "> but got <" + value + ">");
            }
        }
    }

    /**
     * This record represents an expectation that a value should be lesser than the max value.
     *
     * @param <T> the type of the value to be checked
     * @param max the maximum value
     */
    record LesserThan<T>(T max) implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            var c = Utils.asComparableOrThrow(
                    value,
                    () -> new AssertionError("Value is not comparable")
            );

            if (c.compareTo(max) >= 0) {
                throw new AssertionError("Expected value to be lesser than <" + max + "> but got <" + value + ">");
            }
        }
    }

    /**
     * This record represents an expectation that a value should be one of the values in the collection.
     *
     * @param <T>    the type of the value to be checked
     * @param values the collection of values
     */
    record OneOf<T>(Collection<T> values) implements ExpectedToBe<T> {
        @Override
        public void verify(T value) {
            if (!values.contains(value))
                throw new AssertionError("Expected value to be one of <" + values + "> but got <" + value + ">");
        }
    }

}
