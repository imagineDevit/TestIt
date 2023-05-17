package io.github.imagineDevit.testIt;

import io.github.imagine.devit.TestIt.*;
import org.junit.jupiter.api.Assertions;


@TestItClass
public class MyTest {

    @TestIt("(1 * 2) + 1 should be 3")
    void test(TestCase<Integer,Integer> testCase){
        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state.map(i -> i * 2).orElse(0))
                .when("1 is added to the state", state -> state.map(i -> i + 1).orElse(0))
                .then("the result should be 3", result -> {
                    Assertions.assertTrue(result.isPresent());
                    Assertions.assertEquals(3, result.get());
                });
    }

    @ParameterizedTestIt(
            name = "(1 * 2) + {0} should be equal to {1}",
            source = "getParams")
    void test2(TestCase<Integer,Integer> testCase, Integer number, Integer expectedResult){

        testCase
                .given("state is 1", () -> 1)
                .and("state is multiplied by 2", state -> state.map(i -> i * 2).orElse(0))
                .when("%d is added to the state".formatted(number), state -> state.map(i -> i + number).orElse(0))
                .then("the result should be %d".formatted(expectedResult), result -> {
                    Assertions.assertTrue(result.isPresent());
                    Assertions.assertEquals(expectedResult, result.get());
                });
    }
    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams(){
        return TestParameters.of(
                TestParameters.Parameter.P2.of(1, 3),
                TestParameters.Parameter.P2.of(2, 4)
        );
    }

}
