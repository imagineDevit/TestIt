package io.github.imagineDevit.giwt.expectations;

/**
 * This interface provides a set of static methods to create different types of failure expectations.
 * Each failure expectation is a record that implements the ExpectedToFail interface and overrides the verify method.
 * The verify method is used to check if an exception meets the expectation.
 * If the exception does not meet the expectation, an AssertionError is thrown.
 */
public sealed interface ExpectedToFail extends Expectation.OnFailure {

    /**
     * Creates a failure expectation that an exception should be of a specific type.
     *
     * @param clazz the class of the expected exception
     * @return a WithType failure expectation
     */
    static WihType withType(Class<?> clazz) {
        return new WihType(clazz);
    }

    /**
     * Creates a failure expectation that an exception should have a specific message.
     *
     * @param message the expected message
     * @return a WithMessage failure expectation
     */
    static WithMessage withMessage(String message) {
        return new WithMessage(message);
    }

    /**
     * This record represents a failure expectation that an exception should be of a specific type.
     *
     * @param clazz the class of the expected exception
     */
    record WihType(Class<?> clazz) implements ExpectedToFail {
        @Override
        public void verify(Exception e) {
            if (!clazz.isInstance(e)) {
                throw new AssertionError("Expected error to be of type <" + clazz.getName() + "> but got <" + e.getClass().getName() + ">");
            }
        }
    }

    /**
     * This record represents a failure expectation that an exception should have a specific message.
     *
     * @param message the expected message
     */
    record WithMessage(String message) implements ExpectedToFail {
        @Override
        public void verify(Exception e) {
            if (!e.getMessage().equals(message)) {
                throw new AssertionError("Expected error message to be <" + message + "> but got <" + e.getMessage() + ">");
            }

        }
    }
}
