package io.github.imagineDevit.giwt.expectations;


import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.annotations.Test;

import java.util.List;

import static io.github.imagineDevit.giwt.expectations.ExpectedToFail.withMessage;
import static io.github.imagineDevit.giwt.expectations.ExpectedToFail.withType;
import static io.github.imagineDevit.giwt.expectations.ExpectedToMatch.*;

class ExpectedToMatchTest {

    @Test
    void testOne(TestCase<Matching<String>, Void> testCase) {
        testCase
                .given("a matching with a string 'a'", matching("expected to be <a>", s -> s.equals("a")))
                .when("verify it", matching -> { one(matching).verify("a");})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testOne2(TestCase<Matching<String>, Void> testCase) {
        var desc = "expected to be <a>";
        testCase
                .given("a matching with a string 'a'", matching(desc, s -> s.equals("a")))
                .when("verify it", matching -> {one(matching).verify("b");})
                .then("verification should succeed", result ->
                        result
                            .shouldFail(withType(AssertionError.class))
                            .and(withMessage("Matching <%s> failed".formatted(desc)))
                );
    }


    @Test
    void testAll(TestCase<List<Matching<String>>, Void> testCase) {
        testCase
                .given("some matchings on a string 'a'",
                        List.of(
                                matching("expected to be <a>", s -> s.equals("a")),
                                matching("expected to have length 1", s -> s.length() == 1)
                        )
                )
                .when("verify it", matchings -> { new All<>(matchings).verify("a");})
                .then("verification should succeed", Expectable::shouldSucceed);

    }

    @Test
    void testAll2(TestCase<List<Matching<String>>, Void> testCase) {
        String desc1 = "expected to be <a>";
        String desc2 = "expected to have length 1";
        testCase
                .given("some matchings on a string 'a'",
                        List.of(
                                matching(desc1, s -> s.equals("a")),
                                matching(desc2, s -> s.length() == 1)
                        )
                )
                .when("verify it", matchings -> { new All<>(matchings).verify("b");})
                .then("verification should fail", result ->
                        result
                            .shouldFail(withType(AssertionError.class))
                            .and(withMessage("Matching <%s> failed".formatted(desc1)))
                );

    }

    @Test
    void testAll3(TestCase<List<Matching<String>>, Void> testCase) {
        String desc1 = "expected to be <a>";
        String desc2 = "expected to have length 1";
        testCase
                .given("some matchings on a string 'a'",
                        List.of(
                                matching(desc1, s -> s.length() == 1),
                                matching(desc2, s -> s.equals("a"))
                        )
                )
                .when("verify it", matchings -> { new All<>(matchings).verify("b");})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Matching <%s> failed".formatted(desc2)))
                );

    }


    @Test
    void testNone(TestCase<List<Matching<String>>, Void> testCase) {
        testCase
                .given("some matchings on a string 'a'",
                        List.of(
                                matching("expected to be <a>", s -> s.equals("a")),
                                matching("expected to have length 1", s -> s.length() == 1)
                        )
                )
                .when("verify it", matchings -> { new None<>(matchings).verify("ab");})
                .then("verification should succeed", Expectable::shouldSucceed);

    }

    @Test
    void testNone2(TestCase<List<Matching<String>>, Void> testCase) {
        String desc1 = "expected to be <ab>";
        String desc2 = "expected to have length 1";
        testCase
                .given("some matchings on a string 'a'",
                        List.of(
                                matching(desc1, s -> s.equals("ab")),
                                matching(desc2, s -> s.length() == 1)
                        )
                )
                .when("verify it", matchings -> { new None<>(matchings).verify("ab");})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Matching <%s> failed".formatted(desc1)))
                );

    }

    @Test
    void testNone3(TestCase<List<Matching<String>>, Void> testCase) {
        String desc1 = "expected to be <a>";
        String desc2 = "expected to have length 1";
        testCase
                .given("some matchings on a string 'a'",
                        List.of(
                                matching(desc1, s -> s.equals("a")),
                                matching(desc2, s -> s.length() == 2)
                        )
                )
                .when("verify it", matchings -> { new None<>(matchings).verify("ab");})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Matching <%s> failed".formatted(desc2)))
                );

    }
}