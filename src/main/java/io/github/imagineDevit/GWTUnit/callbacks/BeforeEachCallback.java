package io.github.imagineDevit.GWTUnit.callbacks;

/**
 * This functional interface represents a callback that is executed before each test method
 */
@FunctionalInterface
public interface BeforeEachCallback {
    void beforeEach();
}
