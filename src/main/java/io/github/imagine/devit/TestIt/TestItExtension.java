package io.github.imagine.devit.TestIt;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


/**
 *
 */
public class TestItExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("beforeEach");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {

    }
}
