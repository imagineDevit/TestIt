package io.github.imagineDevit.giwt.callbacks;

/**
 * Represents a callback function to be executed after each test.
 */
@FunctionalInterface
public non-sealed interface AfterEachCallback extends Callback {
    void afterEach();
}
