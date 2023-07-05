package io.github.imagineDevit.GWTUnit.callbacks;

/**
 * A functional interface that represents a callback to be executed before all test cases in a test suite.
 */
@FunctionalInterface
public interface BeforeAllCallback {
    void beforeAll();
}
