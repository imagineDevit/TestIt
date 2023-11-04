package io.github.imagineDevit.giwt.utils;

import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.annotations.Test;

import java.util.Optional;

import static io.github.imagineDevit.giwt.utils.Matchers.MatchCase.matchCase;
import static io.github.imagineDevit.giwt.utils.Matchers.Result.failure;
import static io.github.imagineDevit.giwt.utils.Matchers.Result.success;
import static io.github.imagineDevit.giwt.utils.Matchers.match;


class MatchersTest {

    public static final String VALUE = "value";

    @Test("One success case should return optional of value")
    void testMatch(TestCase<Matchers.MatchCase<String>, Optional<String>> testCase) {
        testCase
                .when("""
                                  match function is called with one success case that has a predicate that always returns true
                                """,
                        () -> match(matchCase(() -> true, () -> success(VALUE)))
                )
                .then("the result should be non empty optional", result -> result.shouldBe().equalTo(Optional.of(VALUE)));
    }

    @Test("One failure case should return empty optional")
    void testMatch2(TestCase<Matchers.MatchCase<String>, Optional<String>> testCase) {
        testCase
                .when("""
                                  match function is called with one failure case that has a predicate that always returns true
                                """,
                        () -> match(matchCase(() -> true, () -> failure("")))
                )
                .then("the result should be empty optional", result -> result.shouldBe().equalTo(Optional.empty()));
    }

    @Test()
    void testMatch3(TestCase<Matchers.MatchCase<String>, Optional<String>> testCase) {
        testCase
                .when("""
                                match function is called with:
                                     - one success case that has a predicate that always returns true
                                     - and a list of failures cases that have predicates that always return false
                                """,
                        () -> match(
                                matchCase(() -> false, () -> failure("error1")),
                                matchCase(() -> true, () -> success(VALUE)),
                                matchCase(() -> false, () -> failure("error2"))
                                ))
                .then("the result should be optional of value", result -> result.shouldBe().equalTo(Optional.of(VALUE)));

    }

    @Test()
    void testMatch4(TestCase<Void, RuntimeException> testCase) {
        testCase
                .when("""
                                match function is called with:
                                     - one success case that has a predicate that always returns true
                                     - one failure case that has a predicate that always return true
                                     - and a list of failures cases that have predicates that always return false
                                """,
                        () -> match(
                                matchCase(() -> true, () -> failure("error1")),
                                matchCase(() -> true, () -> success(VALUE)),
                                matchCase(() -> false, () -> failure("error2"))
                        )
                )
                .then("the result should be a runtime exception", result ->
                        result
                                .shouldFail()
                                .withErrorOfType(RuntimeException.class)
                                .withMessage("error1")
                );

    }
}