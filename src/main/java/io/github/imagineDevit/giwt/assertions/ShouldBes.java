package io.github.imagineDevit.giwt.assertions;

import io.github.imagineDevit.giwt.core.utils.Utils;

import java.util.Collection;

@SuppressWarnings({"unused"})
public sealed interface ShouldBes<T> extends Expectation.Succeed<T> {

    static <T> Null<T> null_() {
        return new Null<>();
    }

    static <T> NotNull<T> notNull() {
        return new NotNull<>();
    }

    static <T> EqualTo<T> equalTo(T expected) {
        return new EqualTo<>(expected);
    }

    static <T> NotEqualTo<T> notEqualTo(T expected) {
        return new NotEqualTo<>(expected);
    }

    static <T> Between<T> between(T min, T max) {
        return new Between<>(min, max);
    }

    static <T> GreaterThan<T> greaterThan(T min) {
        return new GreaterThan<>(min);
    }

    static <T> LesserThan<T> lesserThan(T max) {
        return new LesserThan<>(max);
    }

    static <T> OneOf<T> oneOf(Collection<T> values) {
        return new OneOf<>(values);
    }

    record Null<T>() implements ShouldBes<T> {
        @Override
        public void verify(T value) {
            if (value != null) {
                throw new AssertionError("Expected null value but got " + value);
            }
        }
    }

    record NotNull<T>() implements ShouldBes<T> {
        @Override
        public void verify(T value) {
            if (value == null) {
                throw new AssertionError("Expected not null value but got <null>");
            }
        }
    }

    record EqualTo<T>(T expected) implements ShouldBes<T> {
        @Override
        public void verify(T value) {
            if (!value.equals(expected)) {
                throw new AssertionError("Expected value to be <" + expected + "> but got <" + value + ">");
            }
        }
    }

    record NotEqualTo<T>(T expected) implements ShouldBes<T> {
        @Override
        public void verify(T value) {
            if (value.equals(expected)) {
                throw new AssertionError("Expected value to be different from <" + expected + "> but got <" + value + ">");
            }
        }
    }

    record Between<T>(T min, T max) implements ShouldBes<T> {
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

    record GreaterThan<T>(T min) implements ShouldBes<T> {
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

    record LesserThan<T>(T max) implements ShouldBes<T> {
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

    record OneOf<T>(Collection<T> values) implements ShouldBes<T> {
        @Override
        public void verify(T value) {
            if (!values.contains(value))
                throw new AssertionError("Expected value to be one of <" + values + "> but got <" + value + ">");
        }
    }

}
