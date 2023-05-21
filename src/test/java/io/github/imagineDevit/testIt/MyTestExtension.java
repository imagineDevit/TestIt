package io.github.imagineDevit.testIt;

import io.github.imagine.devit.TestIt.callbacks.AfterAllCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeAllCallback;

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
