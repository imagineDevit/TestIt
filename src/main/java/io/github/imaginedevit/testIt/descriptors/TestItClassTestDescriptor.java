package io.github.imaginedevit.testIt.descriptors;

import io.github.imaginedevit.testIt.engine.TestItPredicates;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

public class TestItClassTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;

    public TestItClassTestDescriptor(Class<?> testClass, UniqueId uniqueId) {
        super(
                uniqueId.append("class", testClass.getSimpleName()),
                testClass.getSimpleName(),
                ClassSource.from(testClass)
        );

        this.testClass = testClass;
        addAllChildren();
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    private void addAllChildren() {
        ReflectionUtils.findMethods(testClass, TestItPredicates.isMethodTest())
                .forEach(method -> addChild(new TestItMethodTestDescriptor(method, testClass, getUniqueId())));
    }
}
