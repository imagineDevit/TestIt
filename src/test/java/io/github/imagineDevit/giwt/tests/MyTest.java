package io.github.imagineDevit.giwt.tests;


import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.annotations.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith({MyTestExtension.class})
@ConfigureWith(MyTestConfiguration.class)
@SuppressWarnings("unused")
class MyTest {

    int i;

    @BeforeAll
    void setUp() {
        i = 1;
        System.out.println("Before All I'm executed ****************");
    }

    @AfterAll
    void tearDown() {
        System.out.println("at the end => i = " + i);
    }

    @Test("(1 * 2) + 1 should be 3")
    void test(TestCase<Integer, Integer> testCase) {
        i++;
        System.out.println("i = " + i);

        testCase
                .given("state is 1", 1)
                .and("state is multiplied by 2", state -> state * 2)
                .when("1 is added to the state", i -> i + 1)
                .then("the result should be not null", result -> result.shouldBe().notNull())
                .and("the result should be equal to 3", result -> result.shouldBe().equalTo(3));
    }

    @ParameterizedTest(
            name = "(1 * 2) + {0} should be equal to {1}",
            source = "getParams"
    )
    void test2(TestCase<Integer, Integer> testCase, Integer number, Integer expectedResult) {
        i++;

        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", i -> i * 2)
                .when("%d is added to the state".formatted(number), i -> i + number)
                .then("the result should be %d".formatted(expectedResult), result ->
                        result
                                .shouldBe()
                                .notNull()
                                .equalTo(expectedResult)
                );
    }

    @Test
    @Skipped(reason = "this test is skipped")
    void test3(TestCase<Void, Integer> testCase) {
        testCase
                .when("called method return 1", () -> 1)
                .then("the result should be not null", result -> result.shouldBe().notNull())
                .and("the result should be equal to 1", result -> result.shouldBe().equalTo(1));
    }

    @Test("An illegalState exception should be thrown")
    void test4(TestCase<Void, Void> testCase) {
        testCase
                .when("called method throw an exception with oups message", () -> {
                    throw new IllegalStateException("Oups");
                })
                .then("the exception is not null", result ->
                        result
                                .shouldFail()
                                .withErrorOfType(IllegalStateException.class)
                                .withMessage("Oups")
                );
    }

    @Test("test case with context")
    void test5(TestCase<Integer, Integer> testCase) {
        testCase.withContext()
                .given("the state is set to 1", ctx -> ctx.setState(1))
                .when("result is set to state + 1", ctx -> ctx.mapToResult(one -> one + 1))
                .then("the result should be 2", (ctx, result) ->
                        result.shouldBe()
                                .notNull()
                                .equalTo(2)
                );
    }

    @Test("Add element to an empty collection")
    void test6(TestCase<List<String>, List<String>> testCase) {
        testCase.withContext()
                .given("an empty list", new ArrayList<>())
                .when("an element is added to the list", ctx -> ctx
                        .applyOnState(list -> list.add("element"))
                        .setStateAsResult()
                )
                .then("the result should be not null", (ctx, result) -> result.shouldBe().notNull())
                .and("the result should have a size equal to 1", (ctx, result) -> result.shouldHave().size(1))
                .and("the result should contain an item equal to 'element'", (ctx, result) -> result.shouldHave().anItemEqualTo("element"));
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
