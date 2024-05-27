package io.github.imagineDevit.giwt.expectations;


import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.annotations.Test;

import java.util.NoSuchElementException;

import static io.github.imagineDevit.giwt.expectations.ExpectedToFail.*;

class ExpectedToFailTest {

    @Test("an illegal state exception should be of type IllegalStateException")
    void testWithType(TestCase<Exception, Void> testCase) {
        testCase.given("an illegal state exception", new IllegalStateException())
                .when("verify it", e -> {
                    withType(IllegalStateException.class).verify(e);
                })
                .then("no exception is thrown", Expectable::shouldSucceed);
    }

    @Test("no such element exception should not be of type IllegalStateException")
    void testWithType2(TestCase<Exception, Void> testCase) {
        testCase.given("a no such element exception", new NoSuchElementException())
                .when("verify it", e -> {
                    withType(IllegalStateException.class).verify(e);
                })
                .then("an assertion error should be thrown", result ->
                    result.shouldFail(withType(AssertionError.class))
                            .and(withMessage("Expected error to be of type <java.lang.IllegalStateException> but got <java.util.NoSuchElementException>"))
                );
    }

    @Test("an exception with message 'Illegal state' should have the same message")
    void testWithMessage(TestCase<Exception, Void> testCase) {
        String message = "Illegal state";
        testCase.given("an illegal state exception", new Exception(message))
                .when("verify it", e -> { withMessage(message).verify(e); })
                .then("no exception is thrown", Expectable::shouldSucceed);
    }

    @Test("an exception with message 'Illegal state' should have the message 'Null pointer' ")
    void testWithMessage2(TestCase<Exception, Void> testCase) {
        testCase.given("an illegal state exception", new Exception("Illegal state"))
                .when("verify it", e -> { withMessage("Null pointer").verify(e); })
                .then("an assertion error should be thrown", result -> result
                        .shouldFail(withType(AssertionError.class))
                            .and(withMessage("Expected error message to be <Null pointer> but got <Illegal state>"))
                );
    }

}