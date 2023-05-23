package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.annotations.AfterEach;
import io.github.imagineDevit.GWTUnit.annotations.BeforeEach;
import io.github.imagineDevit.GWTUnit.annotations.ParameterizedTest;
import io.github.imagineDevit.GWTUnit.annotations.Test;


public class MyTest2 {

    int i;

    @BeforeEach
    void setUp() {
        i += 2;
    }

    @AfterEach
    void tearDown() {
        i = 0;
    }

    @Test
    void test3(TestCase<Integer, Integer> testCase) {
        System.out.println("i = " + i);

        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state * 2)
                .when("1 is added to the state", state -> state.mapAndGet(i -> i + 1))
                .then("the result should be 3", result ->
                        result
                                .shouldBeNotNull()
                                .shouldBeEqualTo(3)
                );
    }

    @ParameterizedTest(
            name = "(1 * 2) + {0} should be equal to {1}",
            source = "getParams")
    void test4(TestCase<Integer, Integer> testCase, Integer number, Integer expectedResult) {

        System.out.println("i = " + i);
        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state * 2)
                .when("%d is added to the state".formatted(number), state -> state.mapAndGet(i -> i + number))
                .then("the result should be %d".formatted(expectedResult), result ->
                    result
                            .shouldBeNotNull()
                            .shouldBeEqualTo(expectedResult)
                );
    }

    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams() {
        return TestParameters.of(
                TestParameters.Parameter.P2.of(1, 3),
                TestParameters.Parameter.P2.of(2, 4)
        );
    }

}
