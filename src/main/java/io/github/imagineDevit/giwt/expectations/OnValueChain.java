
package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.expectations.Expectable;
import io.github.imagineDevit.giwt.core.expectations.Expectation;
import io.github.imagineDevit.giwt.core.expectations.ExpectationChain;

/**
 * The OnValueChain class represents a chain of expectations that are verified with a value.
 *
 * @param <T> the type of the value
 * @param <E> the type of the expectation
 * @author imagineDevit
 */
public class OnValueChain<T, E extends Expectation.OnValue<T>> extends ExpectationChain.OnValue<T, E> {

    // The expectable to be used
    private final Expectable<T> expectable;

    /**
     * Constructs a new OnValueChain instance with the given value and expectable.
     *
     * @param value      the value to be verified
     * @param expectable the expectable to be used
     */
    public OnValueChain(T value, Expectable<T> expectable) {
        super(value);
        this.expectable = expectable;
    }

    /**
     * Returns the expectable used by this OnValueChain.
     *
     * @return the expectable used by this OnValueChain
     */
    public Expectable<T> and() {
        return this.expectable;
    }
}
