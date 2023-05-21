package io.github.imagine.devit.TestIt.descriptors;

import io.github.imagine.devit.TestIt.annotations.*;
import io.github.imagine.devit.TestIt.callbacks.AfterAllCallback;
import io.github.imagine.devit.TestIt.callbacks.AfterEachCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeAllCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeEachCallback;
import io.github.imagine.devit.TestIt.utils.TestItPredicates;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

import static io.github.imagine.devit.TestIt.utils.Utils.*;

public class TestItClassTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;

    private final Object testInstance;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    public TestItClassTestDescriptor(Class<?> testClass, UniqueId uniqueId) {
        super(
                uniqueId.append("class", testClass.getSimpleName()),
                testClass.getSimpleName(),
                ClassSource.from(testClass)
        );

        this.testClass = testClass;

        this.testInstance = ReflectionUtils.newInstance(testClass);


        this.beforeAllCallback = () ->
                runCallbacks(
                        getBeforeAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeAll.class)).map(BeforeAll::order).orElse(0)
                );

        this.afterAllCallback = () ->
                runCallbacks(
                        getAfterAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterAll.class)).map(AfterAll::order).orElse(0)
                );

        this.beforeEachCallback = () ->
                runCallbacks(
                        getBeforeEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeEach.class)).map(BeforeEach::order).orElse(0)
                );


        this.afterEachCallback = () ->
                runCallbacks(
                        getAfterEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterEach.class)).map(AfterEach::order).orElse(0)
                );


        addAllChildren();
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public void execute(Consumer<TestItClassTestDescriptor> consumer) {
        beforeAllCallback.beforeAll();
        consumer.accept(this);
        afterAllCallback.afterAll();
    }

    private void addAllChildren() {
        ReflectionUtils.findMethods(testClass, TestItPredicates.isMethodTest())
                .forEach(method ->
                        addChild(new TestItMethodTestDescriptor(
                                getTestName(method.getAnnotation(Test.class).value(), method),
                                method,
                                testInstance,
                                getUniqueId(),
                                null, beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback))
                );

        ReflectionUtils.findMethods(testClass, TestItPredicates.isParameterizedMethodTest())
                .forEach(method -> {
                    String parameterSource = method.getAnnotation(ParameterizedTest.class).source();
                    Method sourceMethod = ReflectionUtils.findMethod(testClass, parameterSource).orElseThrow();
                    addChild(new TestItParameterizedMethodTestDescriptor(method, sourceMethod, testInstance, getUniqueId(), beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback));
                });
    }
}
