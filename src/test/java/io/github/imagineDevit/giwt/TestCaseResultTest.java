package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.annotations.Test;

import static io.github.imagineDevit.giwt.core.errors.ResultValueError.*;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.equalTo;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.notNull;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToFail.withMessage;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToFail.withType;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToMatch.one;

class TestCaseResultTest {


    @Test("create a TestCaseResult with a given value")
    void of(TestCase<Integer, TestCaseResult<Integer>> tc) {
        tc
                .given(" a not null number 1 ", 1)
                .when("a TestCaseResult is created using 'of' method factory", (i) -> {
                    return TestCaseResult.of(i);
                })
                .then("the value of the TestCaseResult should be 1", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseResult.of(1)));
                });
    }

    @Test("create a TestCaseResult with a null value")
    void of2(TestCase<Integer, TestCaseResult<Integer>> tc) {
        tc
                .given(" a null number", () -> null)
                .when("a TestCaseResult is created using 'of' method factory", (i) -> {
                    return TestCaseResult.of(i);
                })
                .then("the value of the TestCaseResult should be null", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseResult.of(null)));
                });
    }

    @Test("create a TestCaseResult with a given exception")
    void ofErr(TestCase<Exception, TestCaseResult<?>> tc) {
        tc.withContext()
                .given(" a not null exception ", new Exception())
                .when("a TestCaseResult is created using 'ofErr' method factory", (ctx, e) -> TestCaseResult.ofErr(e))
                .then("the value of the TestCaseResult should be the exception", (ctx, result) ->
                        result
                                .shouldBe(notNull())
                                .and(equalTo(TestCaseResult.ofErr(ctx.getState().value())))
                );
    }

    @Test("create a TestCaseResult with a null exception")
    void ofErr2(TestCase<Exception, TestCaseResult<?>> tc) {
        tc
                .given(" a null exception ", () -> null)
                .when("a TestCaseResult is created using 'ofErr' method factory", (e) -> {
                    return TestCaseResult.ofErr(e);
                })
                .then("the value of the TestCaseResult should be the exception", (result) ->
                        result
                                .shouldFail(withType(NullPointerException.class))
                );
    }

    @Test("create a empty TestCaseResult")
    void empty(TestCase<Integer, TestCaseResult<Integer>> tc) {
        tc
                .when("a TestCaseResult is created using 'empty' method factory", () -> TestCaseResult.empty())
                .then("the value of the TestCaseResult should be null", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseResult.of(null)));
                });
    }

    @Test("map a TestCaseResult with a value")
    void map(TestCase<TestCaseResult<Integer>, TestCaseResult<String>> tc) {
        tc
                .given(" a TestCaseResult with a value of 1 ", TestCaseResult.of(1))
                .when("the value of the TestCaseResult is mapped to a string", (result) -> {
                    return result.map(Object::toString);
                })
                .then("the value of the TestCaseResult should be '1'", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and(equalTo(TestCaseResult.of("1")));
                });
    }

    @Test("map a TestCaseResult with an exception")
    void map2(TestCase<TestCaseResult<Integer>, TestCaseResult<String>> tc) {
        tc
                .given(" a TestCaseResult with a error ", TestCaseResult.ofErr(new Exception()))
                .when("the value of the TestCaseResult is mapped to a string", (result) -> {
                    return result.map(Object::toString);
                })
                .then("the TestCaseResult should fail", (result) -> {
                    result
                            .shouldFail(withType(ExpectedValueFailed.class))
                            .and(withMessage(EXPECTED_VALUE_FAILED));
                });
    }


    @Test("get the value of a TestCaseResult with a value")
    void resultValue(TestCase<TestCaseResult<Integer>, Integer> tc) {
        tc
                .given(" a TestCaseResult with a value of 1 ", TestCaseResult.of(1))
                .when("the value of the TestCaseResult is retrieved", (result) -> {
                    return result.resultValue();
                })
                .then("the value should be 1", (value) -> {
                    value
                            .shouldBe(notNull())
                            .and(equalTo(1));
                });
    }

    @Test("try get the value of a TestCaseResult with an exception")
    void resultValue2(TestCase<TestCaseResult<Integer>, Integer> tc) {
        tc
                .given(" a TestCaseResult with an exception", TestCaseResult.ofErr(new Exception()))
                .when("the value of the TestCaseResult is retrieved", (result) -> {
                    return result.resultValue();
                })
                .then("the value should fail", (value) -> {
                    value
                            .shouldFail(withType(ExpectedValueFailed.class))
                            .and(withMessage(EXPECTED_VALUE_FAILED));
                });
    }


    @Test("get the error of a TestCaseResult with an exception")
    void resultError(TestCase<TestCaseResult<Object>, Throwable> tc) {
        tc
                .given(" a TestCaseResult with an exception", TestCaseResult.ofErr(new Exception()))
                .when("the error of the TestCaseResult is retrieved", (result) -> {
                    return result.resultError();
                })
                .then("the error should be the exception", (result) -> {
                    result
                            .shouldBe(notNull())
                            .and()
                            .shouldMatch(one("expected to instance of Exception", e -> e instanceof Exception));
                });
    }

    @Test("try get the error of a TestCaseResult with a value")
    void resultError2(TestCase<TestCaseResult<Integer>, Throwable> tc) {
        tc
                .given(" a TestCaseResult with a value", TestCaseResult.of(1))
                .when("the value of the TestCaseResult is retrieved", (result) -> {
                    return result.resultError();
                })
                .then("the value should fail", (result) -> {
                    result
                            .shouldFail(withType(ExpectedErrorFailed.class))
                            .and(withMessage(EXPECTED_ERROR_FAILED));
                });
    }
}