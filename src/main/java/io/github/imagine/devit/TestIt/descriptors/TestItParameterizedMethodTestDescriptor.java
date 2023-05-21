package io.github.imagine.devit.TestIt.descriptors;

import io.github.imagine.devit.TestIt.TestParameters;
import io.github.imagine.devit.TestIt.annotations.ParameterizedTest;
import io.github.imagine.devit.TestIt.callbacks.AfterAllCallback;
import io.github.imagine.devit.TestIt.callbacks.AfterEachCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeAllCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeEachCallback;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.List;

public class TestItParameterizedMethodTestDescriptor extends AbstractTestDescriptor {

    private final Method testMethod;
    private final Method parameterSourceMethod;

    private final Object testInstance;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    public TestItParameterizedMethodTestDescriptor(Method testMethod, Method parameterSourceMethod, Object testInstance, UniqueId uniqueId, BeforeAllCallback beforeAllCallback, AfterAllCallback afterAllCallback, BeforeEachCallback beforeEachCallback, AfterEachCallback afterEachCallback) {

        super(
                uniqueId.append("method", testMethod.getName()),
                testMethod.getName(),
                MethodSource.from(testMethod)
        );

        this.testInstance = testInstance;
        this.testMethod = testMethod;
        this.parameterSourceMethod = parameterSourceMethod;
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

    @SuppressWarnings("unchecked")
    private List<TestParameters.Parameter> getParams(){

        Object instance = ReflectionUtils.newInstance(this.testMethod.getDeclaringClass());

        TestParameters<TestParameters.Parameter> testParameters = (TestParameters<TestParameters.Parameter>) ReflectionUtils.invokeMethod(this.parameterSourceMethod, instance);

        return testParameters.getParameters();
    }

    private void addAllChildren() {
        getParams().forEach(p -> {
            String name = p.formatName(this.testMethod.getAnnotation(ParameterizedTest.class).name());
            addChild(new TestItMethodTestDescriptor(name, this.testMethod, this.testInstance, getUniqueId(), p, beforeAllCallback, afterAllCallback, beforeEachCallback, afterEachCallback));
        });
    }

}
