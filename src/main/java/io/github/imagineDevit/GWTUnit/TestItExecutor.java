package io.github.imagineDevit.GWTUnit;

import io.github.imagineDevit.GWTUnit.descriptors.TestItClassTestDescriptor;
import io.github.imagineDevit.GWTUnit.descriptors.TestItMethodTestDescriptor;
import io.github.imagineDevit.GWTUnit.descriptors.TestItParameterizedMethodTestDescriptor;
import io.github.imagineDevit.GWTUnit.report.ReportViewer;
import io.github.imagineDevit.GWTUnit.report.TestCaseReport;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.util.Arrays;
import java.util.Optional;

public class TestItExecutor {

    private TestCaseReport report;

    private static Integer NB = null;

    private boolean allCallbacksRan = false;

    private Boolean withReport;

    public void execute(ExecutionRequest request, TestDescriptor root) {

        if (withReport == null) {
            withReport = Boolean.valueOf(System.getenv(MvnArgs.GENERATE_REPORT));
        }

        if (NB == null) {
            NB = root.getChildren().size();
        }

        if (root instanceof EngineDescriptor) {

            initReport();

            executeForEngineDescriptor(request, root);
        }

        if (root instanceof TestItClassTestDescriptor ctd) {
            allCallbacksRan = true;
            ctd.execute(d -> executeForClassDescriptor(request, d));
            allCallbacksRan = false;
        }

        if (root instanceof TestItParameterizedMethodTestDescriptor) {
            executeContainer(request, root);
        }

        if (root instanceof TestItMethodTestDescriptor mtd) {
            mtd.execute(d ->  executeForMethodDescriptor(request, mtd), allCallbacksRan);
        }

    }

    private void initReport() {
        if (report == null && withReport) {
            report = new TestCaseReport();
        }
    }


    private void executeForMethodDescriptor(ExecutionRequest request, TestItMethodTestDescriptor md) {
        String className = md.getTestMethod().getDeclaringClass().getName();

        Optional<TestCaseReport.ClassReport> classReport = getReport().map(
                tc -> tc.getClassReport(className).orElseGet(() -> {
                    TestCaseReport.ClassReport cr = new TestCaseReport.ClassReport(className);
                    tc.addClassReport(cr);
                    return cr;
                })
        );


        TestCaseReport.TestReport testReport = executeTest(request, md);
        classReport.ifPresent(cr -> cr.addTestReport(testReport));
    }

    private void executeForClassDescriptor(ExecutionRequest request, TestItClassTestDescriptor r) {
        TestCaseReport.ClassReport classReport = new TestCaseReport.ClassReport(r.getTestClass().getName());
        getReport().ifPresent(tc -> tc.addClassReport(classReport));
        executeContainer(request, r);
    }

    private void executeForEngineDescriptor(ExecutionRequest request, TestDescriptor root) {
        executeContainer(request, root);
        getReport()
                .ifPresent(tc -> {
                    if (NB != null && tc.getClassReports().size() == NB) {
                        try {
                            new ReportViewer().view(tc);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            NB = null;
                        }
                    }
                });
    }

    private TestCaseReport.TestReport executeTest(ExecutionRequest request, TestItMethodTestDescriptor root) {

        TestCaseReport.TestReport report = new TestCaseReport.TestReport();

        TestCase<?, ?> testCase = root.getTestCase(report, TestCase::create, TestCase::getName);

        EngineExecutionListener listener = request.getEngineExecutionListener();

        listener.executionStarted(root);

        return root.shouldBeSkipped()
                .map(reason -> {
                    listener.executionSkipped(root, reason);
                    return report;
                })
                .orElseGet(() -> {
                    try {

                        if (root.getParams() != null) {
                            root.getParams().executeTest(root.getTestInstance(), root.getTestMethod(), testCase);
                        } else {
                            ReflectionUtils.invokeMethod(root.getTestMethod(), root.getTestInstance(), testCase);
                        }

                        TestCase.class.getDeclaredMethod("run").invoke(testCase);

                        report.setStatus(TestCaseReport.TestReport.Status.SUCCESS);

                        System.out.println(TestCase.Result.SUCCESS.message(null));

                        listener.executionFinished(root, TestExecutionResult.successful());

                    } catch (Exception e) {

                        report.setStatus(TestCaseReport.TestReport.Status.FAILURE);

                        report.addTrace(e.getClass().getName());

                        Arrays.stream(e.getStackTrace()).forEach(element -> report.addTrace("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at %s".formatted(element.toString())));

                        if (e.getCause() != null) {
                            System.out.println(TestCase.Result.FAILURE.message(e.getCause().getMessage()));
                            report.setFailureReason(e.getCause().getMessage());
                            report.addTrace("Caused by: %s : %s".formatted(e.getCause().getClass().getName(), e.getCause().getMessage()));
                            Arrays.stream(e.getCause().getStackTrace()).forEach(element -> report.addTrace("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at %s".formatted(element.toString())));
                        } else {
                            System.out.println(TestCase.Result.FAILURE.message(e.getMessage()));
                            report.setFailureReason(e.getMessage());
                        }

                        listener.executionFinished(root, TestExecutionResult.failed(e));
                    }

                    return report;
                });
    }

    private void executeContainer(ExecutionRequest request, TestDescriptor root) {
        EngineExecutionListener listener = request.getEngineExecutionListener();

        listener.executionStarted(root);

        root.getChildren().forEach(child -> execute(request, child));

        listener.executionFinished(root, TestExecutionResult.successful());
    }

    private Optional<TestCaseReport> getReport() {
        return Optional.ofNullable(report);
    }
}
