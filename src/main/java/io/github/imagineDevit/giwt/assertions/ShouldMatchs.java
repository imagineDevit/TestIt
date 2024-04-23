package io.github.imagineDevit.giwt.assertions;

import io.github.imagineDevit.giwt.core.utils.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings({"unused"})
public sealed interface ShouldMatchs<T> extends Expectation.Succeed<T> {

    static <T> Matching<T> matching(String description, Predicate<T> predicate) {
        return new Matching<>(description, predicate);
    }

    static <T> One<T> one(Matching<T> matching) {
        return new One<>(matching);
    }

    @SafeVarargs
    static <T> All<T> all(Matching<T>... matchings) {
        return new All<>(Arrays.asList(matchings));
    }

    @SafeVarargs
    static <T> None<T> none(Matching<T>... matchings) {
        return new None<>(Arrays.asList(matchings));
    }

    record Matching<T>(String description, Predicate<T> predicate) {
        public void shouldTest(T value) {
            if (!predicate.test(value)) {
                throw new AssertionError("Matching < %s > failed ".formatted(TextUtils.yellow(description)));
            }
        }
    }

    record One<T>(Matching<T> matching) implements ShouldMatchs<T> {
        @Override
        public void verify(T value) {
            matching.shouldTest(value);
        }
    }

    record All<T>(List<Matching<T>> matchings) implements ShouldMatchs<T> {
        @Override
        public void verify(T value) {
            List<String> failedMessages = matchings.stream()
                    .filter(matching -> !matching.predicate.test(value))
                    .map(m -> "👉 %s".formatted(m.description))
                    .toList();

            if (!failedMessages.isEmpty()) {
                throw new AssertionError("\n Following matchings failed: \n %s  \n".formatted(String.join("\n ", failedMessages)));
            }

        }
    }

    record None<T>(List<Matching<T>> matchings) implements ShouldMatchs<T> {
        @Override
        public void verify(T value) {
            List<String> failedMessages = matchings.stream()
                    .filter(matching -> matching.predicate.test(value))
                    .map(m -> "👉 %s".formatted(m.description))
                    .toList();

            if (!failedMessages.isEmpty()) {
                throw new AssertionError("\n Following matchings are expected to fail but succeed: \n %s  \n".formatted(String.join("\n ", failedMessages)));
            }
        }
    }
}
