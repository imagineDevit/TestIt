package io.github.imagine.devit.TestIt;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.lang.reflect.Method;

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
                .forEach(method ->
                    addChild(new TestItMethodTestDescriptor(
                            Utils.getTestItName(method.getAnnotation(TestIt.class).value(), method),
                            method,
                            testClass,
                            getUniqueId(),
                            null))
                );

        ReflectionUtils.findMethods(testClass, TestItPredicates.isParameterizedMethodTest())
                .forEach(method -> {
                    String parameterSource = method.getAnnotation(ParameterizedTestIt.class).source();
                    Method sourceMethod = ReflectionUtils.findMethod(testClass, parameterSource).orElseThrow();
                    addChild(new TestItParameterizedMethodTestDescriptor(method, sourceMethod, testClass, getUniqueId()));
                });
    }
}
