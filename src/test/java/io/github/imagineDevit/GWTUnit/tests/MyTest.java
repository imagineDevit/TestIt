package io.github.imagineDevit.GWTUnit.tests;

import io.github.imagineDevit.GWTUnit.TestCase;
import io.github.imagineDevit.GWTUnit.TestCaseResult;
import io.github.imagineDevit.GWTUnit.TestParameters;
import io.github.imagineDevit.GWTUnit.annotations.*;


@ExtendWith({MyTestExtension.class})
@ConfigureWith(MyTestConfiguration.class)
@SuppressWarnings("unused")
public class MyTest {

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
        System.out.println("i = " + i);
        i++;

        testCase
                .given("state is 1",  1)
                .and("state is multiplied by 2", state -> state.map(i -> i * 2))
                .when("1 is added to the state", state -> state.onValue(i -> i + 1))
                .then("the result should be not null", TestCaseResult::shouldBeNotNull)
                .and("the result should be equal to 3", result -> result.shouldBeEqualTo(3));
    }

    @ParameterizedTest(
            name = "(1 * 2) + {0} should be equal to {1}",
            source = "getParams"
    )
    void test2(TestCase<Integer, Integer> testCase, Integer number, Integer expectedResult) {

        System.out.println("i = " + i);
        i++;

        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state.map(i -> i * 2))
                .when("%d is added to the state".formatted(number), state -> state.onValue(i -> i + number))
                .then("the result should be %d".formatted(expectedResult), result ->
                        result
                                .shouldBeNotNull()
                                .shouldBeEqualTo(expectedResult)
                );
    }

    @Test
    @Skipped(reason = "this test is skipped")
    void test3(TestCase<Void, Integer> testCase) {
        testCase
                .when("called method return 1", () -> 1)
                .then("the result should be not null", TestCaseResult::shouldBeNotNull)
                .and("the result should be equal to 1", result -> result.shouldBeEqualTo(1));
    }

    @Test("An illegalState exception should be thrown")
    void test4(TestCase<Void, IllegalStateException> testCase) {
        testCase
                .when("called method throw an exception with oups message", () -> {
                    throw new IllegalStateException("Oups");
                })
                .then("the exception is not null", result -> result.shouldBeException(IllegalStateException.class));
    }

    @Test("test case with context")
    void test5(TestCase<Integer, Integer> testCase) {
        testCase.withContext()
                .given("the state is set to 1", ctx -> ctx.setState(1))
                .when("result is set to state + 1", ctx -> ctx.stateToResult(one -> one + 1))
                .then("the result should be 2", (ctx, result) ->
                        result
                                .shouldBeNotNull()
                                .shouldBeEqualTo(2)
                );
    }

    @ParameterSource("getParams")
    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams() {
        return TestParameters.of(
                TestParameters.Parameter.P2.of(1, 3),
                TestParameters.Parameter.P2.of(2, 4),
                TestParameters.Parameter.P2.of(4, 6)
        );
    }

}
