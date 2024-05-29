package io.github.imagineDevit.giwt.tests;


import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.imagineDevit.giwt.core.expectations.ExpectedToBe.*;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToFail.withMessage;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToFail.withType;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToHave.anItemEqualTo;
import static io.github.imagineDevit.giwt.core.expectations.ExpectedToHave.size;

@ExtendWith({MyTestExtension.class})
@ConfigureWith(MyTestConfiguration.class)
@SuppressWarnings("unused")
class MyTest {

    int i;

    @BeforeAll
    void setUp() {
        i = 1;
    }

    @AfterAll
    void tearDown() {
        System.out.println("at the end => i = " + i);
    }

    @Test("(1 * 2) + 1 should be 3")
    void test(TestCase<Integer, Integer> testCase) {
        testCase
                .given("state is 1", 1)
                .and("state is multiplied by 2", t -> i++)
                .when("1 is added to the state", i -> (i * 2) + 1)
                .then("the result should be not null", result -> result.shouldBe(notNull()))
                .and("the result should be equal to 3", result -> result.shouldBe(equalTo(3)));
    }

    @ParameterizedTest(
            name = "(1 * 2) + {0} should be equal to {1}",
            source = "getParams"
    )
    void test2(TestCase<Integer, Integer> testCase, Integer number, Integer expectedResult) {
        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", t -> i++)
                .when("%d is added to the state".formatted(number), i -> (i * 2) + number)
                .then("the result should be %d".formatted(expectedResult), result -> result.shouldBe(notNull(), equalTo(expectedResult)));
    }

    @Test
    @Skipped(reason = "this test is skipped")
    void test3(TestCase<Void, Integer> testCase) {
        testCase
                .when("called method return 1", () -> 1)
                .then("the result should be not null and equal to 1",
                        result -> result
                                .shouldBe(notNull())
                                .and(equalTo(1))
                );
    }

    @Test("An illegalState exception should be thrown")
    void test4(TestCase<Void, Void> testCase) {
        testCase
                .when("called method throw an exception with oups message", () -> {
                    throw new IllegalStateException("Oups");
                })
                .then("the exception is not null",
                        result -> result
                                .shouldFail(withType(IllegalStateException.class))
                                .and(withMessage("Oups"))

                );
    }

    @Test("test case with context")
    void test5(TestCase<Integer, Integer> testCase) {
        testCase.withContext()
                .given("the state is set to 1", ctx -> ctx.setState(1))
                .when("result is set to state + 1", ctx -> ctx.mapToResult(one -> one + 1))
                .then("the result should be 3", (ctx, result) -> result.shouldBe(notNull(), equalTo(2)));
    }

    @Test("Add element to an empty collection")
    void test6(TestCase<List<String>, List<String>> testCase) {
        testCase.withContext()
                .given("an empty list", new ArrayList<>())
                .and("element is stored as context variable", ctx -> ctx.setVar("var", "element"))
                .when("an element is added to the list", ctx -> {
                    ctx.applyOnState(list -> list.add(ctx.getVar("var")));
                    ctx.setStateAsResult();
                })
                .then("the result should be not null", (ctx, result) -> result.shouldBe(notNull()))
                .and("the result should have a single item equal to 'element'",
                        (ctx, result) -> result
                                        .shouldHave(size(1))
                                        .and(anItemEqualTo("element"))
                );
    }

    @Test("ctx An illegalState exception should be thrown")
    void test7(TestCase<Void, Void> testCase) {
        testCase.withContext()
                .when("called method throw an exception with oups message", (ctx) -> {
                    throw new IllegalStateException("Oups");
                })
                .then("the exception is not null", (ctx, result) -> result.shouldFail(withType(IllegalStateException.class), withMessage("Oups")));
    }

    @ParameterSource
    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams() {
        return TestParameters.of(
                TestParameters.Parameter.P2.of(1, 3),
                TestParameters.Parameter.P2.of(2, 4),
                TestParameters.Parameter.P2.of(4, 6)
        );
    }

}
