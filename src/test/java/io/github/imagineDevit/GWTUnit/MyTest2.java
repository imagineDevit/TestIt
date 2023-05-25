package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.annotations.*;

@ConfigureWith(MyTestConfiguration.class)
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
                .and("state is multiplied by 2", state -> state.map(i -> i * 2))
                .when("1 is added to the state", state -> state.onValue(i -> i + 1))
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
                .and("state is multiplied by 2", state -> state.map(i -> i * 2))
                .when("%d is added to the state".formatted(number), state -> state.onValue(i -> i + number))
                .then("the result should be %d".formatted(expectedResult), result ->
                    result
                            .shouldBeNotNull()
                            .shouldBeEqualTo(expectedResult)
                );
    }

}
