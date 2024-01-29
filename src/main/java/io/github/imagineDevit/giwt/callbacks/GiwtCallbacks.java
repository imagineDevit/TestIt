package io.github.imagineDevit.giwt.callbacks;

public record GiwtCallbacks(
        BeforeAllCallback beforeAllCallback,
        AfterAllCallback afterAllCallback,
        BeforeEachCallback beforeEachCallback,
        AfterEachCallback afterEachCallback
) {}
