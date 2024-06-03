package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.annotations.Test;

import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.equalTo;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.notNull;

class TestCaseStateTest {

    @Test("create a TestCaseState with a not null given value")
    void of(TestCase<Integer, TestCaseState<Integer>> tc) {
        tc
                .given(" a not null number 1 ", 1)
                .when("a TestCaseState is created using 'of' method factory", (i) -> {
                    return TestCaseState.of(i);
                })
                .then("the value of the TestCaseState should be 1", (result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseState.of(1))));
    }

    @Test("create a TestCaseState with a null value")
    void of2(TestCase<Integer, TestCaseState<Integer>> tc) {
        tc
                .given(" a not null number", () -> null)
                .when("a TestCaseState is created using 'of' method factory", (i) -> {
                    return TestCaseState.of(i);
                })
                .then("the value of the TestCaseState should be null", (result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseState.of(null))));
    }

    @Test("create a TestCaseState with no initial value")
    void empty(TestCase<Void, TestCaseState<Integer>> tc) {
        tc
                .when("a TestCaseState is created using 'empty' method factory",
                        () -> TestCaseState.empty()
                )
                .then("the value of the TestCaseState should be null", (result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseState.of(null))));
    }
}