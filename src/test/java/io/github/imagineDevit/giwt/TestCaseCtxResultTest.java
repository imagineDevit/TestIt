package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.annotations.Test;

import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.equalTo;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.notNull;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToFail.withType;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToMatch.one;

class TestCaseCtxResultTest {


    @Test("create a TestCaseCtxResult with a given value")
    void of(TestCase<Integer, TestCaseCtxResult<Integer>> tc) {
        tc
                .given(" a not null number 1 ", 1)
                .when("a TestCaseCtxResult is created using 'of' method factory", (i) -> {
                    return TestCaseCtxResult.of(i);
                })
                .then("the value of the TestCaseCtxResult should be 1", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseCtxResult.of(1)));
                });
    }

    @Test("create a TestCaseCtxResult with a null value")
    void of2(TestCase<Integer, TestCaseCtxResult<Integer>> tc) {
        tc
                .given(" a null number", () -> null)
                .when("a TestCaseCtxResult is created using 'of' method factory", (i) -> {
                    return TestCaseCtxResult.of(i);
                })
                .then("the value of the TestCaseCtxResult should be null", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseCtxResult.of(null)));
                });
    }

    @Test("create a TestCaseCtxResult with a given exception")
    void ofErr(TestCase<Exception, TestCaseCtxResult<?>> tc) {
        tc.withContext()
                .given(" a not null exception ", new Exception())
                .when("a TestCaseCtxResult is created using 'ofErr' method factory", (ctx, e) -> TestCaseCtxResult.ofErr(e))
                .then("the value of the TestCaseCtxResult should be the exception", (ctx, result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseCtxResult.ofErr(ctx.getState().value())))
                );
    }

    @Test("create a TestCaseCtxResult with a null exception")
    void ofErr2(TestCase<Exception, TestCaseCtxResult<?>> tc) {
        tc
                .given(" a null exception ", () -> null)
                .when("a TestCaseCtxResult is created using 'ofErr' method factory", (e) -> {
                    return TestCaseCtxResult.ofErr(e);
                })
                .then("the value of the TestCaseCtxResult should be the exception", (result) ->
                        result
                                .shouldFail(withType(NullPointerException.class))
                );
    }

    @Test("create a empty TestCaseCtxResult")
    void empty(TestCase<Integer, TestCaseCtxResult<Integer>> tc) {
        tc
                .when("a TestCaseCtxResult is created using 'empty' method factory", () -> TestCaseCtxResult.empty())
                .then("the value of the TestCaseCtxResult should be null", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseCtxResult.of(null)));
                });
    }

    @Test("get the result of a TestCaseCtxResult")
    void result(TestCase<TestCaseCtxResult<Integer>, TestCaseResult<Integer>> tc) {

        tc
                .given("a TestCaseCtxResult with a value", () -> TestCaseCtxResult.of(1))
                .when("the result method is called", (result) -> {
                    return result.result();
                })
                .then("the result should be a TestCaseResult with the same value", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseResult.of(1)));
                });
    }

    @Test("get the result of a TestCaseCtxResult with an exception")
    void result2(TestCase<TestCaseCtxResult<Integer>, TestCaseResult<Integer>> tc) {

        tc
                .given("a TestCaseCtxResult with an exception", () -> TestCaseCtxResult.ofErr(new Exception()))
                .when("the result method is called", (result) -> {
                    return result.result();
                })
                .then("the result should be a TestCaseResult with the same exception", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and()
                            .shouldMatch(one("Expected to have error of type Exception", e -> e.resultError() instanceof Exception));
                });
    }
}