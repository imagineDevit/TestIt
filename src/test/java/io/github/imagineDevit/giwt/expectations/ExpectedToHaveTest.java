package io.github.imagineDevit.giwt.expectations;


import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.github.imagineDevit.giwt.expectations.ExpectedToFail.*;
import static io.github.imagineDevit.giwt.expectations.ExpectedToHave.anItemEqualTo;
import static io.github.imagineDevit.giwt.expectations.ExpectedToHave.size;

class ExpectedToHaveTest {

    @Test
    void testSize(TestCase<String, Void> testCase) {
        testCase.given("a string with 3 characters", "abc")
                .when("verify it", value -> {size(3).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testSize2(TestCase<List<Object>, Void> testCase) {
        testCase.given("a list with 3 items", List.of(1, "2", true))
                .when("verify it", value -> {size(3).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testSize3(TestCase<Map<String, Object>, Void> testCase) {
        testCase.given("a map with 3 entries",() -> Map.of("1", 1, "2", "2", "3", true))
                .when("verify it", value -> {size(3).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testSize4(TestCase<Object[], Void> testCase) {
        testCase.given("an array with 3 items", new Object[]{1, "2", true})
                .when("verify it", value -> {size(3).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }


    @Test
    void testSize5(TestCase<Object, Void> testCase) {
        testCase.given("an object", new Object())
                .when("verify it", value -> {size(3).verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(IllegalStateException.class))
                                .and(withMessage("Result value has no size"))
                );
    }

    @Test
    void testSize6(TestCase<String, Void> testCase) {
        testCase.given("an string with 3 characters", "abc")
                .when("verify it", value -> {size(4).verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected result to have size <4> but got <3>"))
                );
    }


    @Test
    void testAnItemEqualTo(TestCase<List<Object>, Void> testCase) {
        testCase.given("a list with 3 items", List.of(1, "2", true))
                .when("verify it", value -> {anItemEqualTo("2").verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testAnItemEqualTo2(TestCase< Map<String, Object>, Void> testCase) {
        testCase.given("a map with 3 entries",() -> Map.of("1", 1, "2", "2", "3", true))
                .when("verify it", value -> {anItemEqualTo(1).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testAnItemEqualTo3(TestCase<Object[], Void> testCase) {
        testCase.given("an array with 3 items", new Object[]{1, "2", true})
                .when("verify it", value -> {anItemEqualTo(true).verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testAnItemEqualTo4(TestCase<String , Void> testCase) {
        testCase.given("a string with 3 items", "abc")
                .when("verify it", value -> {anItemEqualTo("a").verify(value);})
                .then("verification should succeed", Expectable::shouldSucceed);
    }

    @Test
    void testAnItemEqualTo5(TestCase<Object, Void> testCase) {
        testCase.given("an object", new Object())
                .when("verify it", value -> {anItemEqualTo(3).verify(value);})
                .then("verification should fail", result ->
                        result
                                .shouldFail(withType(IllegalStateException.class))
                                .and(withMessage("Result value is not a collection, an array, a map or a string"))
                );
    }

    @Test
    void testAnItemEqualTo6(TestCase<String , Void> testCase) {
        testCase.given("a string with 3 items", "abc")
                .when("verify it", value -> {anItemEqualTo("d").verify(value);})
                .then("verification should succeed", result ->
                        result.shouldFail(withType(AssertionError.class))
                        .and(withMessage("Expected result to contain <d> but it does not")));
    }

    @Test
    void testAnItemEqualTo7(TestCase<Object[] , Void> testCase) {
        testCase.given("an array with 3 items", new Object[]{1, "2", true})
                .when("verify it", value -> {anItemEqualTo(false).verify(value);})
                .then("verification should succeed", result ->
                        result.shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected result to contain <false> but it does not")));
    }

    @Test
    void testAnItemEqualTo8(TestCase<Map<String, Object> , Void> testCase) {
        testCase.given("a map with 3 entries",() -> Map.of("1", 1, "2", "2", "3", true))
                .when("verify it", value -> {anItemEqualTo("1").verify(value);})
                .then("verification should succeed", result ->
                        result.shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected result to contain <1> but it does not")));
    }

    @Test
    void testAnItemEqualTo9(TestCase<List<Object> , Void> testCase) {
        testCase.given("a list with 3 items", List.of(1, "2", true))
                .when("verify it", value -> {anItemEqualTo(3).verify(value);})
                .then("verification should succeed", result ->
                        result.shouldFail(withType(AssertionError.class))
                                .and(withMessage("Expected result to contain <3> but it does not")));
    }
}