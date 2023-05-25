package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.annotations.*;
import io.github.imagineDevit.GWTUnit.annotations.ParameterSource;


@ExtendWith({MyTestExtension.class})
@ConfigureWith(MyTestConfiguration.class)
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

    // region tests
    @Test("(1 * 2) + 1 should be 3")
    @Skipped
    void test(TestCase<Integer, Integer> testCase) {
        System.out.println("i = " + i);
        i++;

        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state.map(i -> i * 2))
                .when("1 is added to the state", state -> state.onValue(i -> i + 1))
                .then("the result should be not null", TestCaseResult::shouldBeNotNull)
                .and("the result should be equal to 3", result -> result.shouldBeEqualTo(3));
    }

    @ParameterizedTest(
            name = "(1 * 2) + {0} should be equal to {1}",
            source = "getParams")
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

    // endregion

    @Test("An illegalState exception should be thrown")
    void test4(TestCase<Void, IllegalStateException> testCase) {
        testCase
                .when("called method throw an exception with oups message", () -> {throw new IllegalStateException("Oups");})
                .then("the exception is not null", result -> result.shouldBeException(IllegalStateException.class));
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
