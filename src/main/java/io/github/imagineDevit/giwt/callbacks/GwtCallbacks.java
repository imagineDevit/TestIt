package io.github.imagineDevit.giwt.callbacks;

public record GwtCallbacks(
        BeforeAllCallback beforeAllCallback,
        AfterAllCallback afterAllCallback,
        BeforeEachCallback beforeEachCallback,
        AfterEachCallback afterEachCallback
) {}
