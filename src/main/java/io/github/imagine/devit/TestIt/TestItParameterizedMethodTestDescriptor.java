package io.github.imagine.devit.TestIt;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.List;

public class TestItParameterizedMethodTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;
    private final Method testMethod;
    private final Method parameterSourceMethod;

    public TestItParameterizedMethodTestDescriptor(Method testMethod, Method parameterSourceMethod, Class<?> testClass, UniqueId uniqueId) {

        super(
                uniqueId.append("method", testMethod.getName()),
                testMethod.getName(),
                MethodSource.from(testMethod)
        );

        this.testClass = testClass;
        this.testMethod = testMethod;
        this.parameterSourceMethod = parameterSourceMethod;
        addAllChildren();
    }


    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    @SuppressWarnings("unchecked")
    private List<TestParameters.Parameter> getParams(){

        Object instance = ReflectionUtils.newInstance(testClass);

        TestParameters<TestParameters.Parameter> testParameters = (TestParameters<TestParameters.Parameter>) ReflectionUtils.invokeMethod(this.parameterSourceMethod, instance);

        return testParameters.getParameters();
    }

    private void addAllChildren() {
        getParams().forEach(p -> {
            String name = p.formatName(this.testMethod.getAnnotation(ParameterizedTestIt.class).name());
            addChild(new TestItMethodTestDescriptor(name, this.testMethod, this.testClass, getUniqueId(), p));
        });
    }

}
