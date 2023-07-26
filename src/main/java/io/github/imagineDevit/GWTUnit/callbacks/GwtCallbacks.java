package io.github.imagineDevit.GWTUnit.callbacks;

public record GwtCallbacks(
        BeforeAllCallback beforeAllCallback,
        AfterAllCallback afterAllCallback,
        BeforeEachCallback beforeEachCallback,
        AfterEachCallback afterEachCallback
) {}
