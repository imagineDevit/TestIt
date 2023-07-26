package io.github.imagineDevit.GWTUnit.descriptors;

import io.github.imagineDevit.GWTUnit.TestConfiguration;
import io.github.imagineDevit.GWTUnit.annotations.*;
import io.github.imagineDevit.GWTUnit.callbacks.*;
import io.github.imagineDevit.GWTUnit.utils.TestItPredicates;
import io.github.imagineDevit.GWTUnit.utils.Utils;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.util.Optional;
import java.util.function.Consumer;

public class TestItClassTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;

    private final Object testInstance;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    private final TestConfiguration configuration;

    public TestItClassTestDescriptor(Class<?> testClass, UniqueId uniqueId) {
        super(
                uniqueId.append("class", testClass.getSimpleName()),
                testClass.getSimpleName(),
                ClassSource.from(testClass)
        );

        this.testClass = testClass;

        this.testInstance = ReflectionUtils.newInstance(testClass);

        this.beforeAllCallback = () ->
                Utils.runCallbacks(
                        Utils.getBeforeAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeAll.class)).map(BeforeAll::order).orElse(0)
                );

        this.afterAllCallback = () ->
                Utils.runCallbacks(
                        Utils.getAfterAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterAll.class)).map(AfterAll::order).orElse(0)
                );

        this.beforeEachCallback = () ->
                Utils.runCallbacks(
                        Utils.getBeforeEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeEach.class)).map(BeforeEach::order).orElse(0)
                );


        this.afterEachCallback = () ->
                Utils.runCallbacks(
                        Utils.getAfterEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterEach.class)).map(AfterEach::order).orElse(0)
                );

        this.configuration = Optional.ofNullable(testClass.getAnnotation(ConfigureWith.class))
                .map(ConfigureWith::value)
                .map(ReflectionUtils::newInstance)
                .orElse(null);

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

    public boolean shouldBeReported() {
        return !this.configuration.excludeFromReport().contains(this.testClass);
    }

    private void addAllChildren() {
        ReflectionUtils.findMethods(testClass, TestItPredicates.isMethodTest())
                .forEach(method ->
                        addChild(new TestItMethodTestDescriptor(
                                Utils.getTestName(method.getAnnotation(Test.class).value(), method),
                                method,
                                testInstance,
                                getUniqueId(),
                                null, new GwtCallbacks(beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback)))
                );

        ReflectionUtils.findMethods(testClass, TestItPredicates.isParameterizedMethodTest())
                .forEach(method ->
                        addChild(new TestItParameterizedMethodTestDescriptor(method, Utils.getParameters(method, this.configuration), testInstance, getUniqueId(), new GwtCallbacks(beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback), this.configuration))
                );
    }
}
