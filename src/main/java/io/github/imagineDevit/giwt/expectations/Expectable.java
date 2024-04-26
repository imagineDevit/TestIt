package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.Arrays;
import java.util.Optional;

/**
 * This interface provides methods for asserting the state of a result value.
 * It provides several methods for different types of assertions: shouldFail, shouldBe, shouldHave, shouldMatch.
 * Each method takes one or more expectations as arguments and verifies them against the result value.
 *
 * @param <T> the type of the result value
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 * @see ATestCaseResult.ResultValue
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public interface Expectable<T> {

    /**
     * Verifies that the result value should fail the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldFail(ExpectedToFail... expectations) {
        resultError().ifPresentOrElse(
                e -> Arrays.asList(expectations).forEach(f -> f.verify(e)),
                () -> {
                    throw new AssertionError("Result value is Ok");
                }
        );
    }

    /**
     * Verifies that the result value should be the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldBe(ExpectedToBe... expectations) {
        verify(expectations);
    }

    /**
     * Verifies that the result value should have the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldHave(ExpectedToHave... expectations) {
        verify(expectations);
    }

    /**
     * Verifies that the result value should match the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldMatch(ExpectedToMatch... expectations) {
        verify(expectations);
    }

    /**
     * Verifies the provided expectations against the result value.
     *
     * @param expectations the expectations to be checked
     */
    private void verify(Expectation.OnValue[] expectations) {
        resultValue().ifPresentOrElse(
                v -> Arrays.asList(expectations).forEach(b -> b.verify(v)),
                () -> {
                    throw new AssertionError("Result value is Error");
                }
        );
    }

    /**
     * Returns the result value if it is present.
     *
     * @return the result value
     */
    Optional<T> resultValue();

    /**
     * Returns the result error if it is present.
     *
     * @return the result error
     */
    Optional<Exception> resultError();
}