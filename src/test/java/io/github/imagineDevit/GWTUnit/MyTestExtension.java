package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.callbacks.AfterAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeAllCallback;

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
