package io.github.imaginedevit.testIt.descriptors;

import io.github.imaginedevit.testIt.annotations.TestIt;
import io.github.imaginedevit.testIt.TestCase;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;

public class TestItMethodTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;
    private final Method testMethod;

    public TestItMethodTestDescriptor(Method testMethod, Class<?> testClass, UniqueId uniqueId) {

        super(
                uniqueId.append("method", testMethod.getName()),
                testMethod.getAnnotation(TestIt.class).name(),
                MethodSource.from(testMethod)
        );
        this.testClass = testClass;
        this.testMethod = testMethod;
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    public TestCase<?,?> getTestCase() {
        String name = this.testMethod.getAnnotation(TestIt.class).name();
        return TestCase.create(name);
    }

}
