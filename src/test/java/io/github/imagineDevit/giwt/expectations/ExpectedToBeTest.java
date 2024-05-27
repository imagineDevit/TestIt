package io.github.imagineDevit.giwt.expectations;


import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.annotations.Test;

import static io.github.imagineDevit.giwt.expectations.ExpectedToBe.*;
import static io.github.imagineDevit.giwt.expectations.ExpectedToFail.withMessage;
import static io.github.imagineDevit.giwt.expectations.ExpectedToFail.withType;

class ExpectedToBeTest {

    @Test
    void testNull(TestCase<String, Void> testCase) {
        testCase
                .given("a null value", () -> null)
                .when("verify that the value is null", value -> { null_().verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testNull2(TestCase<String, Void> testCase) {
        testCase
                .given("a non null value", () -> "Hello")
                .when("verify that the value is null", value -> { null_().verify(value);})
                .then("verification should fail", result ->
                    result
                            .shouldFail(withType(AssertionError.class))
                            .and(withMessage("Expected <null> but got <Hello>"))
                );
    }


    @Test
    void testNotNull(TestCase<String, Void> testCase) {
        testCase
                .given("a non null value", () -> "Hello")
                .when("verify that the value is not null", value -> { notNull().verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testNotNull2(TestCase<String, Void> testCase) {
        testCase
                .given("a null value", () -> null)
                .when("verify that the value is null", value -> { notNull().verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected not null value but got <null>"))
                );
    }


    @Test
    void testEqualTo(TestCase<String, Void> testCase) {
        testCase
                .given("a value", () -> "Hello")
                .when("verify that the value is equal to Hello", value -> { equalTo("Hello").verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testEqualTo2(TestCase<String, Void> testCase) {
        testCase
                .given("a value", () -> "Hello")
                .when("verify that the value is equal to Hello", value -> { equalTo("World").verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected value to be <World> but got <Hello>"))
                );
    }

    @Test
    void testNotEqualTo(TestCase<String, Void> testCase) {
        testCase
                .given("a value", () -> "Hello")
                .when("verify that the value is equal to Hello", value -> { notEqualTo("World").verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testNotEqualTo2(TestCase<String, Void> testCase) {
        testCase
                .given("a value", () -> "Hello")
                .when("verify that the value is equal to Hello", value -> { notEqualTo("Hello").verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected value to be different from <Hello> but got <Hello>"))
                );
    }

    @Test
    void testBetween(TestCase<Integer, Void> testCase) {
        testCase
                .given("a value", () -> 5)
                .when("verify that the value is between 1 and 10", value -> { between(1, 10).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testBetween2(TestCase<Integer, Void> testCase) {
        testCase
                .given("a value", () -> 0)
                .when("verify that the value is between 1 and 10", value -> { between(1, 10).verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected value to be between <1> and <10> but got <0>"))
                );
    }

    @Test
    void testGreaterThan(TestCase<Integer, Void> testCase) {
        testCase
                .given("a value", () -> 5)
                .when("verify that the value is greater than 1", value -> { greaterThan(1).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testGreaterThan2(TestCase<Integer, Void> testCase) {
        testCase
                .given("a value", () -> 0)
                .when("verify that the value is greater than 1", value -> { greaterThan(1).verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected value to be greater than <1> but got <0>"))
                );
    }

    @Test
    void testLessThan(TestCase<Integer, Void> testCase) {
        testCase
                .given("a value", () -> 5)
                .when("verify that the value is less than 10", value -> { lessThan(10).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testLessThan2(TestCase<Integer, Void> testCase) {
        testCase
                .given("a value", () -> 5)
                .when("verify that the value is less than 0", value -> { lessThan(0).verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected value to be less than <0> but got <5>"))
                );
    }

}