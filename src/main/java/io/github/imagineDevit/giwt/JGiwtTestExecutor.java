package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.GiwtTestExecutor;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.report.TestCaseReport;

/**
 * Giwt test executor
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"rawtypes"})
public class JGiwtTestExecutor extends GiwtTestExecutor<TestCase> {

    public JGiwtTestExecutor() {
        super();
    }

    @Override
    public void run(TestCase testCase) {
        testCase.run();
    }

    @Override
    public TestCase createTestCase(String name, TestCaseReport.TestReport report, TestParameters.Parameter parameter) {
        return new TestCase<>(name, report, parameter);
    }

}
