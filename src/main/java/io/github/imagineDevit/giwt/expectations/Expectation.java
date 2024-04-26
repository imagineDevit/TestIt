package io.github.imagineDevit.giwt.expectations;

/**
 * This interface represents an expectation, which is a condition that a value must satisfy.
 * An expectation can either succeed or fail.
 * It provides a method to verify the expectation against a value.
 *
 * @param <T> the type of the value to be checked
 * @author Henri Joel SEDJAME
 * @version 0.1.5
 */
public sealed interface Expectation<T> {

    /**
     * Verifies the expectation against a value.
     *
     * @param value the value to be checked
     */
    void verify(T value);

    /**
     * This interface represents a failed expectation.
     * It extends the Expectation interface with the type parameter set to Exception.
     * It can only be implemented by the ExpectedToFail class.
     */
    sealed interface OnFailure extends Expectation<Exception> permits ExpectedToFail {
    }

    /**
     * This interface represents a successful expectation.
     * It extends the Expectation interface with a generic type parameter.
     * It can be implemented by the ExpectedToBe, ExpectedToHave, and ExpectedToMatch classes.
     *
     * @param <T> the type of the value to be checked
     */
    sealed interface OnValue<T> extends Expectation<T> permits ExpectedToBe, ExpectedToHave, ExpectedToMatch {
    }
}