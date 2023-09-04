package io.github.imagineDevit.GWTUnit.descriptors;

import io.github.imagineDevit.GWTUnit.TestCase;
import io.github.imagineDevit.GWTUnit.TestParameters;
import io.github.imagineDevit.GWTUnit.annotations.*;
import io.github.imagineDevit.GWTUnit.callbacks.*;
import io.github.imagineDevit.GWTUnit.report.TestCaseReport;
import io.github.imagineDevit.GWTUnit.report.TestCaseReport.TestReport;
import io.github.imagineDevit.GWTUnit.utils.Utils;
import org.assertj.core.util.TriFunction;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.imagineDevit.GWTUnit.utils.Utils.*;

public class TestItMethodTestDescriptor extends AbstractTestDescriptor {

    private final Method testMethod;

    private final Object testInstance;

    private final TestParameters.Parameter params;

    private final BeforeAllCallback beforeAllCallback;

    private final AfterAllCallback afterAllCallback;

    private final BeforeEachCallback beforeEachCallback;

    private final AfterEachCallback afterEachCallback;

    public TestItMethodTestDescriptor(String name, Method testMethod, Object testInstance, UniqueId uniqueId, TestParameters.Parameter params, GwtCallbacks callbacks) {

        super(
                uniqueId.append("method", name),
                name,
                MethodSource.from(testMethod)
        );
        this.testInstance = testInstance;
        this.testMethod = testMethod;
        this.params = params;

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

    }


    public Object getTestInstance() {
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

    public TestCase<?, ?> getTestCase(TestCaseReport.TestReport report, TriFunction<String, TestReport, TestParameters.Parameter, TestCase<?, ?>> createTestCase, Function<TestCase<?, ?>, String> getName) {
        String name;
        if (params == null) {
            name = Utils.getTestName(this.testMethod.getAnnotation(Test.class).value(), this.testMethod);
        } else {
            name = Utils.getTestName(this.testMethod.getAnnotation(ParameterizedTest.class).name(), this.testMethod);
        }


        report.setStatus(TestReport.Status.SKIPPED);

        TestCase<?, ?> tc = createTestCase.apply(name, report, getParams());

        report.setName(getName.apply(tc));

        return tc;
    }

    public Optional<String> shouldBeSkipped() {
        return AnnotationSupport.findAnnotation(this.testMethod, Skipped.class)
                .or(() -> AnnotationSupport.findAnnotation(this.testMethod.getDeclaringClass(), Skipped.class))
                .map(Skipped::reason);
    }

    public void execute(Consumer<TestItMethodTestDescriptor> consumer, boolean allCallacksRan) {
        if (!allCallacksRan) beforeAllCallback.beforeAll();
        beforeEachCallback.beforeEach();
        consumer.accept(this);
        afterEachCallback.afterEach();
        if (!allCallacksRan) afterAllCallback.afterAll();
    }
}
