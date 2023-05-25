package io.github.imagineDevit.GWTUnit.descriptors;

import io.github.imagineDevit.GWTUnit.TestParameters;
import io.github.imagineDevit.GWTUnit.annotations.ParameterizedTest;
import io.github.imagineDevit.GWTUnit.callbacks.AfterAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.AfterEachCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeEachCallback;
import io.github.imagineDevit.GWTUnit.utils.Utils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.List;

public class TestItParameterizedMethodTestDescriptor extends AbstractTestDescriptor {

    private final Method testMethod;
    //private final Method parameterSourceMethod;

    private final Object testInstance;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    private final List<? extends TestParameters.Parameter> parameters;

    public TestItParameterizedMethodTestDescriptor(Method testMethod, List<? extends TestParameters.Parameter> parameters, Object testInstance, UniqueId uniqueId, BeforeAllCallback beforeAllCallback, AfterAllCallback afterAllCallback, BeforeEachCallback beforeEachCallback, AfterEachCallback afterEachCallback) {

        super(
                uniqueId.append("method", testMethod.getName()),
                testMethod.getName(),
                MethodSource.from(testMethod)
        );

        this.testInstance = testInstance;
        this.testMethod = testMethod;
        if (parameters == null) {
            this.parameters = Utils.getParameters(testMethod);
        } else {
            this.parameters = parameters;
        }
        this.beforeAllCallback = beforeAllCallback;
        this.afterAllCallback = afterAllCallback;
        this.beforeEachCallback = beforeEachCallback;
        this.afterEachCallback = afterEachCallback;
        addAllChildren();
    }


    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    private void addAllChildren() {
        parameters.forEach(param -> {
            String name = param.formatName(this.testMethod.getAnnotation(ParameterizedTest.class).name());
            addChild(new TestItMethodTestDescriptor(name, this.testMethod, this.testInstance, getUniqueId(), param, beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback));
        });
    }

}
