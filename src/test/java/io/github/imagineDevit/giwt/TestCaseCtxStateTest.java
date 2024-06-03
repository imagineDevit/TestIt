package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.annotations.Test;

import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.equalTo;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.notNull;

class TestCaseCtxStateTest {

    @Test("create a TestCaseCtxState with a not null given value")
    void of(TestCase<Integer, TestCaseCtxState<Integer>> tc) {
        tc
                .given(" a not null number 1 ", 1)
                .when("a TestCaseState is created using 'of' method factory", (i) -> {
                    return TestCaseCtxState.of(i);
                })
                .then("the value of the TestCaseState should be 1", (result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseCtxState.of(1))));
    }

    @Test("create a TestCaseCtxState with a null value")
    void of2(TestCase<Integer, TestCaseCtxState<Integer>> tc) {
        tc
                .given(" a not null number", () -> null)
                .when("a TestCaseState is created using 'of' method factory", (i) -> {
                    return TestCaseCtxState.of(i);
                })
                .then("the value of the TestCaseState should be null", (result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseCtxState.of(null))));
    }

    @Test("create a TestCaseCtxState with no initial value")
    void empty(TestCase<Void, TestCaseCtxState<Integer>> tc) {
        tc
                .when("a TestCaseState is created using 'empty' method factory",
                        () -> TestCaseCtxState.empty()
                )
                .then("the value of the TestCaseState should be null", (result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseCtxState.of(null))));
    }
}