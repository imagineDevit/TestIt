package io.github.imagine.devit.TestIt;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.util.Arrays;

public class TestItExecutor {

    private TestCaseReport report;

    private static Integer NB = null;

    public void execute(ExecutionRequest request, TestDescriptor root) {
        if (NB == null) {
            NB = root.getChildren().size();
        }
        if (root instanceof EngineDescriptor) {
            if (report == null) {
                report = new TestCaseReport();
            }
            executeContainer(request, root);

            try {
                if(report.getClassReports().size() == NB){
                    new ReportViewer().view(report);
                    NB = null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        if (root instanceof TestItClassTestDescriptor r) {
            TestCaseReport.ClassReport classReport = new TestCaseReport.ClassReport(r.getTestClass().getName());
            report.addClassReport(classReport);
            executeContainer(request, root);
        }

        if (root instanceof TestItParameterizedMethodTestDescriptor){
            executeContainer(request, root);
        }

        if (root instanceof TestItMethodTestDescriptor md) {
            String className = md.getTestMethod().getDeclaringClass().getName();
            TestCaseReport.ClassReport classReport = report.getClassReport(className).orElseGet(() -> {
                TestCaseReport.ClassReport cr = new TestCaseReport.ClassReport(className);
                report.addClassReport(cr);
                return cr;
            });

            TestCaseReport.TestReport testReport = executeTest(request, (TestItMethodTestDescriptor) root);
            classReport.addTestReport(testReport);
        }


    }

    private TestCaseReport.TestReport executeTest(ExecutionRequest request, TestItMethodTestDescriptor root) {


        TestCaseReport.TestReport report = new TestCaseReport.TestReport();

        TestCase<?, ?> testCase = root.getTestCase(report);

        EngineExecutionListener listener = request.getEngineExecutionListener();

        listener.executionStarted(root);

        try {

            Object instance = ReflectionUtils.newInstance(root.getTestClass());

            if (root.getParams() != null){

                root.getParams().executeTest(instance, root.getTestMethod(), testCase);
            } else {
                ReflectionUtils.invokeMethod(root.getTestMethod(), instance, testCase);
            }

            TestCase.class.getDeclaredMethod("run").invoke(testCase);
            report.setStatus(TestCaseReport.TestReport.Status.SUCCESS);

            System.out.println(TestCase.Result.SUCCESS.message(null));

            listener.executionFinished(root, TestExecutionResult.successful());

        } catch (Exception e) {

            report.setStatus(TestCaseReport.TestReport.Status.FAILURE);

            report.addTrace(e.getClass().getName());

            Arrays.stream(e.getStackTrace()).forEach(element -> report.addTrace("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at %s".formatted(element.toString())));

            if (e.getCause() != null){
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
    }

    private void executeContainer(ExecutionRequest request, TestDescriptor root) {
        EngineExecutionListener listener = request.getEngineExecutionListener();

        listener.executionStarted(root);

        root.getChildren().forEach(child -> execute(request, child));

        listener.executionFinished(root, TestExecutionResult.successful());
    }
}
