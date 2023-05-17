package io.github.imagine.devit.TestIt;

import io.github.imagine.devit.TestIt.TestCaseReport.TestReport;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;

public class TestItMethodTestDescriptor extends AbstractTestDescriptor {

    private final Class<?> testClass;
    private final Method testMethod;

    private final TestParameters.Parameter params;

    public TestItMethodTestDescriptor( String name, Method testMethod, Class<?> testClass, UniqueId uniqueId, TestParameters.Parameter params) {

        super(
                uniqueId.append("method", name),
                name,
                MethodSource.from(testMethod)
        );
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.params = params;

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

    public TestParameters.Parameter getParams() {
        return params;
    }

    public TestCase<?,?> getTestCase(TestCaseReport.TestReport report) {
        String name ;
        if (params == null){
           name = Utils.getTestItName(this.testMethod.getAnnotation(TestIt.class).value(), this.testMethod);
        } else {
           name = this.testMethod.getAnnotation(ParameterizedTestIt.class).name();
        }


        report.setStatus(TestReport.Status.SKIPPED);
        TestCase<Object, Object> tc = TestCase.create(name, report, getParams());
        report.setName(tc.getName());
        return tc;
    }

}
