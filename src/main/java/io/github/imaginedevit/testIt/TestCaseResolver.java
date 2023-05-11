package io.github.imaginedevit.testIt;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class TestCaseResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> clazz = parameterContext.getParameter().getType();
        return TestCase.class.isAssignableFrom(clazz);

    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getTestMethod()
                .map(method -> method.getAnnotation(TestIt.class).name())
                //.map(TestCase::create)
                .orElseThrow();
    }
}
