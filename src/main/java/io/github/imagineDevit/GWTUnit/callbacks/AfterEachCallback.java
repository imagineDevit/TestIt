package io.github.imagineDevit.GWTUnit.callbacks;

/**
 * Represents a callback function to be executed after each test.
 */
@FunctionalInterface
public interface AfterEachCallback {
    void afterEach();
}
