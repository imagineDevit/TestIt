package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.annotations.*;


@ExtendWith({MyTestExtension.class})
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
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state * 2)
                .when("1 is added to the state", state ->  state.mapAndGet(i -> i + 1))
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
                .and("state is multiplied by 2", state -> state* 2)
                .when("%d is added to the state".formatted(number), state -> state.mapAndGet(i -> i + number))
                .then("the result should be %d".formatted(expectedResult), result ->
                        result
                                .shouldBeNotNull()
                                .shouldBeEqualTo(expectedResult)

                );
    }

    @Test("An illegalState exception should be thrown")
    void test3(TestCase<Void, IllegalStateException> testCase) {
        testCase
                .when("called method throw an exception with oups message", TestCase.catchItFn(
                        IllegalStateException.class,
                        () -> {
                            throw new IllegalStateException("Oups");
                        })
                )

                .then("the exception is not null", ex -> ex
                        .shouldBeNotNull()
                        .map(IllegalStateException::getMessage)
                        .shouldBeEqualTo("Oups")
                );
    }

    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams() {
        return TestParameters.of(
                TestParameters.Parameter.P2.of(1, 3),
                TestParameters.Parameter.P2.of(2, 4)
        );
    }

}
