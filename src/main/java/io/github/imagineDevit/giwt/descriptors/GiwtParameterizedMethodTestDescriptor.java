package io.github.imagineDevit.giwt.descriptors;

import io.github.imagineDevit.giwt.TestConfiguration;
import io.github.imagineDevit.giwt.TestParameters;
import io.github.imagineDevit.giwt.annotations.*;
import io.github.imagineDevit.giwt.callbacks.*;
import io.github.imagineDevit.giwt.utils.Utils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.github.imagineDevit.giwt.utils.Utils.*;

public class GiwtParameterizedMethodTestDescriptor extends AbstractTestDescriptor {

    private final Method testMethod;
    //private final Method parameterSourceMethod;

    private final Object testInstance;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    private final List<? extends TestParameters.Parameter> parameters;
    private final TestConfiguration configuration;

    public GiwtParameterizedMethodTestDescriptor(Method testMethod, List<? extends TestParameters.Parameter> parameters, Object testInstance, UniqueId uniqueId, GiwtCallbacks callbacks, TestConfiguration configuration) {

        super(
                uniqueId.append("method", testMethod.getName()),
                testMethod.getName(),
                MethodSource.from(testMethod)
        );

        this.testInstance = testInstance;
        this.testMethod = testMethod;
        this.configuration = Objects.requireNonNullElseGet(configuration, () -> Utils.getConfiguration(this.testMethod));
        this.parameters = Objects.requireNonNullElseGet(parameters, () -> Utils.getParameters(this.testMethod, this.configuration));
        this.beforeAllCallback = Objects.requireNonNullElseGet(callbacks.beforeAllCallback(), () -> () ->
                runCallbacks(
                        getBeforeAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeAll.class))
                                .map(BeforeAll::order)
                                .orElse(0)
                )
        );

        this.afterAllCallback = Objects.requireNonNullElseGet(callbacks.afterAllCallback(), () -> () ->
                runCallbacks(
                        getAfterAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterAll.class))
                                .map(AfterAll::order)
                                .orElse(0)
                )
        );

        this.beforeEachCallback = Objects.requireNonNullElseGet(callbacks.beforeEachCallback(), () -> () ->
                runCallbacks(
                        getBeforeEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeEach.class))
                                .map(BeforeEach::order)
                                .orElse(0)
                )
        );

        this.afterEachCallback = Objects.requireNonNullElseGet(callbacks.afterEachCallback(), () -> () ->
                runCallbacks(
                        getAfterEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterEach.class))
                                .map(AfterEach::order)
                                .orElse(0)
                )
        );


        addAllChildren();
    }


    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    private void addAllChildren() {
        parameters.forEach(param -> {
            String name = param.formatName(this.testMethod.getAnnotation(ParameterizedTest.class).name());
            addChild(new GiwtMethodTestDescriptor(name, this.testMethod, this.testInstance, getUniqueId(), param, new GiwtCallbacks(beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback)));
        });
    }

}
