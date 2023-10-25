package io.github.imagineDevit.GWTUnit;


import io.github.imagineDevit.GWTUnit.assertions.ShouldBe;
import io.github.imagineDevit.GWTUnit.assertions.ShouldFail;
import io.github.imagineDevit.GWTUnit.assertions.ShouldHave;
import io.github.imagineDevit.GWTUnit.assertions.ShouldMatch;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestCaseResult<T> {

    public static sealed class ResultValue {

        @SuppressWarnings("unchecked")
        public <T> Optional<Ok<T>> ok() {
            return this instanceof Ok ? Optional.of((Ok<T>) this) : Optional.empty();
        }

        @SuppressWarnings("unchecked")
        public <E extends Exception> Optional<Err<E>> err() {
            return this instanceof Err ? Optional.of((Err<E>) this) : Optional.empty();
        }

        @SuppressWarnings("unchecked")
        public <T> T get() {
            if (this instanceof Ok) {
                return ((Ok<T>) this).getValue();
            }
            throw new RuntimeException("TestCaseResult is not Ok");
        }

        public static final class Ok<T> extends ResultValue {
            private final T value;

            public Ok(T value) {
                this.value = value;
            }

            public T getValue() {
                return value;
            }
        }


        public static final class Err<E extends Exception> extends ResultValue {
            private final E error;

            public Err(E error) {
                this.error = error;
            }

            public E getError() {
                return error;
            }
        }
    }

    private final ResultValue value;

    //region constructor
    private TestCaseResult(T value) {
        this.value = new ResultValue.Ok<>(value);
    }

    private TestCaseResult(Exception e) {
        this.value = new ResultValue.Err<>(e);
    }

    public static <T> TestCaseResult<T> of(T value) {
        return new TestCaseResult<>(value);
    }

    public static <T> TestCaseResult<T> of(Exception e) {
        return new TestCaseResult<>(e);
    }

    public static <T> TestCaseResult<T> empty() {
        return new TestCaseResult<>((T) null);
    }

    //endregion

    public <R> TestCaseResult<R> map(Function<T, R> mapper) {
        return value.<T>ok()
                .map(ResultValue.Ok::getValue)
                .map(v -> TestCaseResult.of(mapper.apply(v)))
                .orElseThrow();
    }

    // region assertions

    public ShouldFail shouldFail() {
        return Objects.requireNonNull(value, "Result value is Null")
                .err()
                .map(ShouldFail::new)
                .orElseThrow(() -> new AssertionError("Result value is Ok"));
    }

    public ShouldBe<T> shouldBe() {
        return Objects.requireNonNull(value, "Result value is Null")
                .<T>ok()
                .map(ShouldBe::new)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));

    }

    public ShouldHave<T> shouldHave() {
        return Objects.requireNonNull(value, "Result value is Null")
                .<T>ok()
                .map(ShouldHave::new)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }

    public ShouldMatch<T> shouldMatch(){
        return Objects.requireNonNull(value, "Result value is Null")
                .<T>ok()
                .map(ShouldMatch::new)
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }

    public void shouldMatch(Predicate<T> predicate){
        Objects.requireNonNull(value, "Result value is Null")
                .<T>ok()
                .map(ShouldMatch::new)
                .map(m -> m.one(ShouldMatch.matching("predicate", predicate)))
                .orElseThrow(() -> new AssertionError("Result value is Failure"));
    }
    // endregion
}
