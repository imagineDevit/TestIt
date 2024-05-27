package io.github.imagineDevit.giwt.expectations;

/**
 * The ExpectationChain interface represents a chain of expectations.
 *
 * @param <T> the type of the value to be verified
 * @param <E> the type of the expectation
 * @author imagineDevit
 */
public sealed interface ExpectationChain<T, E extends Expectation<T>> {

    /**
     * Adds an expectation to the chain.
     *
     * @param expectation the expectation to be added
     * @return the updated ExpectationChain
     */
    ExpectationChain<T,E> and(E expectation);

    /**
     * The OnFailure class represents a chain of expectations that are verified when an error occurs.
     *
     * @param <E> the type of the expectation
     */
    final class OnFailure<T, E extends Expectation.OnFailure> implements ExpectationChain<Throwable, E> {

        private final Throwable error;

        /**
         * Constructs a new OnFailure instance with the given error.
         *
         * @param error the error that occurred
         */
        public OnFailure(Throwable error) {
            this.error = error;
        }

        /**
         * Adds an expectation to the chain and verifies it with the error.
         *
         * @param expectation the expectation to be added
         * @return the updated OnFailure instance
         */
        @Override
        public OnFailure<T,E> and(E expectation) {
            expectation.doVerify(error);
            return this;
        }

    }

    /**
     * The OnValue class represents a chain of expectations that are verified with a value.
     *
     * @param <T> the type of the value
     * @param <E> the type of the expectation
     */
    final class OnValue<T, E extends Expectation.OnValue<T>> implements ExpectationChain<T, E> {

        private final T value;

        private final Expectable<T> expectable;

        /**
         * Constructs a new OnValue instance with the given value.
         *
         * @param value the value to be verified
         */
        public OnValue(T value, Expectable<T> expectable) {
            this.value = value;
            this.expectable = expectable;
        }

        /**
         * Adds an expectation to the chain and verifies it with the value.
         *
         * @param expectation the expectation to be added
         * @return the updated OnValue instance
         */
        @Override
        public OnValue<T, E> and(E expectation) {
            expectation.doVerify(value);
            return this;
        }

        /**
         * Enable chaining with another type of expectation.
         * @return the Expectable instance
         */
        public Expectable<T> and() {
            return this.expectable;
        }
    }
}