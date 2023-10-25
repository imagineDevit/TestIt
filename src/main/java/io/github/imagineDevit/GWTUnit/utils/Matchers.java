package io.github.imagineDevit.GWTUnit.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Matchers {

    public static sealed class Result<T> {
        public boolean isSuccess() {
            return this instanceof Success;
        }

        public static <T> Result<T> success(T value) {
            return new Success<>(value);
        }

        public static <T> Result<T> failure(String message) {
            return new Failure<>(message);
        }

        public static final class Success<T> extends Result<T> {
            private final T value;

            public Success(T value) {
                this.value = value;
            }

            public T getValue() {
                return value;
            }
        }

        public static final class Failure<T> extends Result<T> {
            private final String message;

            public Failure(String message) {
                this.message = message;
            }

            public String getMessage() {
                return message;
            }
        }
    }

    public static class MatchCase<T> {
        private final Supplier<Boolean> predicate;
        private final Supplier<Result<T>> result;

        public MatchCase(Supplier<Boolean> predicate, Supplier<Result<T>> result) {
            this.predicate = predicate;
            this.result = result;
        }

        public static <T> MatchCase<T> matchCase(Supplier<Boolean> predicate, Supplier<Result<T>> result) {
            return new MatchCase<>(predicate, result);
        }


        public boolean matched() {
            return predicate.get();
        }

        public Optional<String> error() {
            return result.get().isSuccess() ? Optional.empty() : Optional.of(((Result.Failure<T>) (result.get())).getMessage());
        }
    }

    @SafeVarargs
    public static <T> Optional<T> match(MatchCase<T>... cases) {

        Map<Boolean, List<MatchCase<T>>> partitions = Arrays.stream(cases)
                .collect(Collectors.partitioningBy(c -> c.result.get().isSuccess()));

        return partitions.get(true)
                .stream()
                .filter(MatchCase::matched)
                .findFirst()
                .map(success -> {
                            partitions.get(false)
                                    .stream()
                                    .filter(MatchCase::matched)
                                    .findFirst()
                                    .ifPresent(matchCase -> {
                                        throw new RuntimeException(matchCase.error().orElse("Unknown error"));
                                    });
                            return ((Result.Success<T>) success.result.get()).getValue();
                        }
                );

    }
}
