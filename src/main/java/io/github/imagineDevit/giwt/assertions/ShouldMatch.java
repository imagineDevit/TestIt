package io.github.imagineDevit.giwt.assertions;

import io.github.imagineDevit.giwt.core.ATestCaseResult;
import io.github.imagineDevit.giwt.core.utils.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @param result the result value
 * @param <T> type of the result value
 * @see ATestCaseResult.ResultValue
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public record ShouldMatch<T>(ATestCaseResult.ResultValue.Ok<T> result) {

    public static <T> Matching<T> matching(String description, Predicate<T> predicate) {
        return new Matching<>(description, predicate);
    }

    public ShouldMatch<T> one(Matching<T> matching) {
        matching.shouldTest(result.getValue());
        return this;
    }

    public ShouldMatch<T> one(String description, Predicate<T> predicate) {
        return one(matching(description, predicate));
    }

    @SafeVarargs
    public final ShouldMatch<T> all(Matching<T>... matchings) {

        List<String> failedMessages = Arrays.stream(matchings)
                .filter(matching -> !matching.predicate.test(result.getValue()))
                .map(m -> "👉 %s".formatted(m.description))
                .toList();

        if (!failedMessages.isEmpty()) {
            throw new AssertionError("\n Following matchings failed: \n %s  \n".formatted(String.join("\n ", failedMessages)));
        }

        return this;
    }

    @SafeVarargs
    public final ShouldMatch<T> none(Matching<T>... matchings) {

        List<String> failedMessages = Arrays.stream(matchings)
                .filter(matching -> matching.predicate.test(result.getValue()))
                .map(m -> "👉 %s".formatted(m.description))
                .toList();

        if (!failedMessages.isEmpty()) {
            throw new AssertionError("\n Following matchings are expected to fail but succeed: \n %s  \n".formatted(String.join("\n ", failedMessages)));
        }

        return this;
    }

    public record Matching<T>(String description, Predicate<T> predicate) {

        public void shouldTest(T value) {
            if (!predicate.test(value)) {
                throw new AssertionError("Matching < %s > failed ".formatted(TextUtils.yellow(description)));
            }
        }
    }

}
