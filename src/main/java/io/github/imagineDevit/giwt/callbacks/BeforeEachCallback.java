package io.github.imagineDevit.giwt.callbacks;

/**
 * This functional interface represents a callback that is executed before each test method
 */
@FunctionalInterface
public interface BeforeEachCallback {
    void beforeEach();
}
