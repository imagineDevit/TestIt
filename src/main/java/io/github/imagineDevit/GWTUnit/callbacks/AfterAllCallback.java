package io.github.imagineDevit.GWTUnit.callbacks;

/**
 * An interface that represents a callback method to be executed after all tests have run.
 */
@FunctionalInterface
public interface AfterAllCallback {
    void afterAll();
}
