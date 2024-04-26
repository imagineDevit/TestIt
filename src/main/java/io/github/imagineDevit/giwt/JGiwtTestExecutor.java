package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.GiwtTestExecutor;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.report.TestCaseReport;

/**
 * This class extends the GiwtTestExecutor class and is used to create a test executor for JGiwt.
 * It is a part of the Giwt testing framework.
 * It is used to execute test cases using the run method and create test cases using the createTestCase method.
 *
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 */
@SuppressWarnings({"rawtypes"})
public class JGiwtTestExecutor extends GiwtTestExecutor<TestCase> {

    /**
     * The constructor for the JGiwtTestExecutor class.
     * It creates a new instance of the JGiwtTestExecutor.
     */
    public JGiwtTestExecutor() {
        super();
    }

    /**
     * This method is used to run a test case.
     *
     * @param testCase The test case to be run.
     */
    @Override
    public void run(TestCase testCase) {
        testCase.run();
    }

    /**
     * This method is used to create a new test case.
     *
     * @param name      The name of the test case.
     * @param report    The report of the test case.
     * @param parameter The parameters of the test case.
     * @return A new instance of TestCase.
     */
    @Override
    public TestCase createTestCase(String name, TestCaseReport.TestReport report, TestParameters.Parameter parameter) {
        return new TestCase<>(name, report, parameter);
    }

}