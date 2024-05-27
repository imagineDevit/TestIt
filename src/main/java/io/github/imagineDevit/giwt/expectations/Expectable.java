package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.ATestCaseResult;

import java.util.Arrays;

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
        Arrays.asList(expectations).forEach(f -> f.doVerify(resultError()));
    }

    /**
     * Verifies that the result value should fail the provided expectation.
     * @param expectation the expectation to be checked
     * @return an ExpectationChain.OnFailure instance
     */
    default ExpectationChain.OnFailure<T, ExpectedToFail> shouldFail(ExpectedToFail expectation) {
        expectation.doVerify(resultError());
        return new ExpectationChain.OnFailure<>(resultError());
    }

    default void shouldSucceed() {
        try {
            resultValue();
        } catch (Throwable e) {
            throw new AssertionError("Expected the result to succeed but got an error: " + e.getMessage());
        }
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
     * Verifies that the result value should be the provided expectation.
     * @param expectation the expectation to be checked
     * @return an ExpectationChain.OnValue instance
     */
    default ExpectationChain.OnValue<T, ExpectedToBe<T>> shouldBe(ExpectedToBe expectation) {
        T value = resultValue();
        expectation.doVerify(value);
       return new ExpectationChain.OnValue<>(value, this);
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
     * Verifies that the result value should have the provided expectation.
     * @param expectation the expectation to be checked
     * @return an ExpectationChain.OnValue instance
     */
    default ExpectationChain.OnValue<T, ExpectedToHave<T>> shouldHave(ExpectedToHave expectation) {
        T value = resultValue();
        expectation.doVerify(value);
        return new ExpectationChain.OnValue<>(value, this);
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
     * Verifies that the result value should match the provided expectation.
     * @param expectation the expectation to be checked
     * @return an ExpectationChain.OnValue instance
     */
    default ExpectationChain.OnValue<T, ExpectedToMatch<T>> shouldMatch(ExpectedToMatch expectation) {
        T value = resultValue();
        expectation.doVerify(value);
        return new ExpectationChain.OnValue<>(value, this);
    }

    /**
     * Verifies the provided expectations against the result value.
     *
     * @param expectations the expectations to be checked
     */
    private void verify(Expectation.OnValue[] expectations) {
        Arrays.asList(expectations).forEach(b -> b.doVerify(resultValue()));

    }

    /**
     * Returns the result value if it is present.
     *
     * @return the result value
     */
    T resultValue();

    /**
     * Returns the result error if it is present.
     *
     * @return the result error
     */
     Throwable resultError();
}