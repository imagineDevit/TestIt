package io.github.imagineDevit.giwt.annotations;

import java.lang.annotation.*;

/**
 * Represents an annotation for specifying the name of a parameter source.
 * This annotation is used to annotate a method that returns a {@link io.github.imagineDevit.giwt.TestParameters} object.
 *
 * <p>Example usage:
 *  <pre>{@code
 *
 *    @ParametersSource("getParams")
 *    private TestParameters<TestParameters.Parameter.P2<Integer, Integer>> getParams() {
 *         return TestParameters.of(
 *                 TestParameters.Parameter.P2.of(1, 3),
 *                 TestParameters.Parameter.P2.of(2, 4),
 *                 TestParameters.Parameter.P2.of(4, 6)
 *         );
 *     }
 *
 *
 *     @ParameterizedTest(
 *             name = "(1 * 2) + {0} should be equal to {1}",
 *             source = "getParams"
 *     )
 *     void test2(TestCase<Integer, Integer> testCase, Integer number, Integer expectedResult) {
 *
 *         testCase
 *                 .given("state is 1", () -> 1)
 *                 .and(
 *                          "state is multiplied by 2",
 *                          state -> state.map(i -> i * 2)
 *                  )
 *                 .when(
 *                          "%d is added to the state".formatted(number),
 *                          state -> state.onValue(i -> i + number)
 *                       )
 *                 .then(
 *                          "the result should be %d".formatted(expectedResult),
 *                          result ->  result.shouldBeNotNull().shouldBeEqualTo(expectedResult)
 *                 );
 *     }
 *    // other test methods
 *  }</pre>
 *
 *
 * @see io.github.imagineDevit.giwt.TestParameters
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ParameterSource {

    /**
     * @return the name of the parameter source
     */
    String value();
}
