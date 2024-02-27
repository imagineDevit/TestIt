package io.github.imagineDevit.giwt.callbacks;

/**
 * A functional interface that represents a callback to be executed before all test cases in a test suite.
 */
@FunctionalInterface
public non-sealed interface BeforeAllCallback extends Callback {
    void beforeAll();
}
