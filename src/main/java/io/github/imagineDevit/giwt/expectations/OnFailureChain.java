package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.expectations.Expectation;
import io.github.imagineDevit.giwt.core.expectations.ExpectationChain;

/**
 * The OnFailureChain class represents a chain of expectations that are verified with an error.
 *
 * @param <E> the type of the expectation
 * @author imagineDevit
 */
public class OnFailureChain<E extends Expectation.OnFailure> extends ExpectationChain.OnFailure<E> {
    /**
     * Constructs a new OnFailure instance with the given error.
     *
     * @param error the error that occurred
     */
    public OnFailureChain(Throwable error) {
        super(error);
    }
}

