package io.github.imagineDevit.giwt.tests;

import io.github.imagineDevit.giwt.core.callbacks.AfterAllCallback;
import io.github.imagineDevit.giwt.core.callbacks.BeforeAllCallback;

public class MyTestExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void afterAll() {
        System.out.println("Extension after all");
    }

    @Override
    public void beforeAll() {
        System.out.println("Extension before all");
    }
}
