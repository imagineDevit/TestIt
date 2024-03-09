package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.ATestCase;
import io.github.imagineDevit.giwt.core.report.TestCaseReport;

import java.util.function.Function;

/**
 * Test case representation
 *
 * @param <T> test case state type
 * @param <R> test case result type
 */
public class TestCase<T, R> extends ATestCase<T, R, TestCaseState<T>, TestCaseResult<R>> {

    private TestCaseWithContext<T, R> ctxCase = null;

    protected TestCase(String name, TestCaseReport.TestReport report, io.github.imagineDevit.giwt.core.TestParameters.Parameter parameters) {
        super(name, report, parameters);
        this.state = TestCaseState.empty();
        this.result = TestCaseResult.empty();
    }

    public TestCaseWithContext<T, R> withContext() {
        this.ctxCase = new TestCaseWithContext<>(this.name, this.report, this.parameters);
        return ctxCase;
    }

    @Override
    protected void run() {

        if (ctxCase != null) {
            ctxCase.run();
            return;
        }

        super.run();
    }

    @Override
    protected TestCaseState<T> stateOf(T value) {
        return TestCaseState.of(value);
    }

    @Override
    protected TestCaseResult<R> stateToResult(Function<T, R> mapper) {
        return this.state.mapToResult(mapper);
    }

    @Override
    protected TestCaseResult<R> resultOf(R value) {
        return TestCaseResult.of(value);
    }

    @Override
    protected TestCaseResult<R> resultOfErr(Exception e) {
        return TestCaseResult.ofErr(e);
    }

}
