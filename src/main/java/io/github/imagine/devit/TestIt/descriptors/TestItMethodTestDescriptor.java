package io.github.imagine.devit.TestIt.descriptors;

import io.github.imagine.devit.TestIt.TestCase;
import io.github.imagine.devit.TestIt.TestParameters;
import io.github.imagine.devit.TestIt.annotations.*;
import io.github.imagine.devit.TestIt.callbacks.AfterAllCallback;
import io.github.imagine.devit.TestIt.callbacks.AfterEachCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeAllCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeEachCallback;
import io.github.imagine.devit.TestIt.utils.Utils;
import io.github.imagine.devit.TestIt.report.TestCaseReport;
import io.github.imagine.devit.TestIt.report.TestCaseReport.TestReport;
import org.assertj.core.util.TriFunction;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.imagine.devit.TestIt.utils.Utils.*;

public class TestItMethodTestDescriptor extends AbstractTestDescriptor {

    private final Method testMethod;

    private final Object testInstance;

    private final TestParameters.Parameter params;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    public TestItMethodTestDescriptor(String name, Method testMethod, Object testInstance, UniqueId uniqueId, TestParameters.Parameter params, BeforeAllCallback beforeAllCallback, AfterAllCallback afterAllCallback, BeforeEachCallback beforeEachCallback, AfterEachCallback afterEachCallback) {

        super(
                uniqueId.append("method", name),
                name,
                MethodSource.from(testMethod)
        );
        this.testInstance = testInstance;
        this.testMethod = testMethod;
        this.params = params;

        this.beforeAllCallback = Objects.requireNonNullElseGet(beforeAllCallback, () -> () ->
                runCallbacks(
                        getBeforeAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeAll.class)).map(BeforeAll::order).orElse(0))
        );

        this.afterAllCallback = Objects.requireNonNullElseGet(afterAllCallback, () -> () ->
                runCallbacks(
                        getAfterAllMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterAll.class)).map(AfterAll::order).orElse(0))
        );

        this.beforeEachCallback = Objects.requireNonNullElseGet(beforeEachCallback, () -> () ->
                runCallbacks(
                        getBeforeEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(BeforeEach.class)).map(BeforeEach::order).orElse(0))
        );

        this.afterEachCallback = Objects.requireNonNullElseGet(afterEachCallback, () -> () ->
                runCallbacks(
                        getAfterEachMethods(testInstance),
                        m -> Optional.ofNullable(m.getAnnotation(AfterEach.class)).map(AfterEach::order).orElse(0))
        );

    }


    public Object getTestInstance(){
        return testInstance;
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    public TestParameters.Parameter getParams() {
        return params;
    }

    public TestCase<?,?> getTestCase(TestCaseReport.TestReport report, TriFunction<String, TestReport, TestParameters.Parameter, TestCase<?,?>> createTestCase, Function<TestCase<?,?>, String> getName) {
        String name ;
        if (params == null){
           name = Utils.getTestName(this.testMethod.getAnnotation(Test.class).value(), this.testMethod);
        } else {
           name = this.testMethod.getAnnotation(ParameterizedTest.class).name();
        }


        report.setStatus(TestReport.Status.SKIPPED);

        TestCase<?, ?> tc = createTestCase.apply(name, report, getParams());

        report.setName(getName.apply(tc));

        return tc;
    }

    public void execute(Consumer<TestItMethodTestDescriptor> consumer, boolean allCallacksRan) {
        if (!allCallacksRan) beforeAllCallback.beforeAll();
        beforeEachCallback.beforeEach();
        consumer.accept(this);
        afterEachCallback.afterEach();
        if (!allCallacksRan) afterAllCallback.afterAll();
    }
}
