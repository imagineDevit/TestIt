package io.github.imaginedevit.testIt.resolvers;

import io.github.imaginedevit.testIt.annotations.TestIt;
import io.github.imaginedevit.testIt.TestCase;
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
        return TestCase.create(extensionContext.getTestMethod().get().getAnnotation(TestIt.class).name());
    }
}
