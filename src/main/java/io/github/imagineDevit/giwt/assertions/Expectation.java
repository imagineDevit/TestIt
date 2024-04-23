package io.github.imagineDevit.giwt.assertions;

/**
 * Expectation representation.
 * Expectation is a condition that must be satisfied by a value.
 * Expectation can be [Succeed] or [Failed].
 * @param <T>
 * @author Henri Joel SEDJAME
 * @since 0.1.5
 */
public sealed interface Expectation<T> {

    /**
     * Verify the expectation.
     * @param value the value to verify
     */
    void verify(T value);


    /**
     * Failure expectation.
     */
    sealed interface Failed extends Expectation<Exception> permits ShouldFails {
    }

    /**
     * Success expectation.
     */
    sealed interface Succeed<T> extends Expectation<T> permits ShouldBes, ShouldHaves, ShouldMatchs {
    }
}
