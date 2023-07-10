package io.github.imagineDevit.GWTUnit;


import io.github.imagineDevit.GWTUnit.report.TestCaseReport;
import io.github.imagineDevit.GWTUnit.statements.StmtMsg;
import io.github.imagineDevit.GWTUnit.statements.functions.CtxConsumer;
import io.github.imagineDevit.GWTUnit.statements.functions.ResCtxConsumer;
import io.github.imagineDevit.GWTUnit.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case representation
 *
 * @param <R> test case result type
 */
public class TestCaseWithContext<T, R> extends CloseableCase {



    // region Statement classes
    public static class GivenCtxStmt<T, R> {

        private final TestCaseWithContext<T, R> testCase;

        public GivenCtxStmt(TestCaseWithContext<T, R> testCase) {
            this.testCase = testCase;
        }

        public GivenCtxStmt<T, R> and(String message, CtxConsumer<R, TestCaseContext<T, R>.GCtx> fn) {
            testCase.andGiven(message, fn);
            return this;
        }

        public  WhenCtxStmt<T, R> when(String message, CtxConsumer<R, TestCaseContext<T, R>.WCtx> fn) {
            return testCase.whenr(message, fn);
        }

    }

    public static class WhenCtxStmt<T, R> {

        private final TestCaseWithContext<T, R> testCase;

        public WhenCtxStmt(TestCaseWithContext<T, R> testCase) {
            this.testCase = testCase;
        }

        public ThenCtxStmt<T, R> then(String message, ResCtxConsumer<R> fn) {
            return testCase.then(message, fn);
        }

    }

    public static class ThenCtxStmt<T, R> {

        private final TestCaseWithContext<T, R> testCase;

        public ThenCtxStmt(TestCaseWithContext<T,R> testCase) {
            this.testCase = testCase;
        }

        public ThenCtxStmt<T, R> and(String message, ResCtxConsumer<R> fn) {
            testCase.andThen(message, fn);
            return this;
        }

    }

    // endregion

    // region Fields

    /**
     * Test case name
     */
    protected final String name;

    /**
     * Test case report
     */
    private final TestCaseReport.TestReport report;

    private final TestCaseContext<T, R>.GCtx gCtx = new TestCaseContext<T, R>().new GCtx();
    private TestCaseContext<T, R>.WCtx wCtx;
    private TestCaseContext<T, R>.TCtx tCtx;


    /**
     * Fns
     */

    private final List<CtxConsumer<R, TestCaseContext<T, R>.GCtx>> givenFns = new ArrayList<>();
    private final List<CtxConsumer<R, TestCaseContext<T, R>.WCtx>> whenFns = new ArrayList<>();
    private final List<ResCtxConsumer<R>> thenFns = new ArrayList<>();

    /**
     * Messages
     */
    private final List<StmtMsg> givenMsgs = new ArrayList<>();
    private final List<StmtMsg> whenMsgs = new ArrayList<>();
    private final List<StmtMsg> thenMsgs = new ArrayList<>();

    private final TestParameters.Parameter parameters;

    // endregion

    // region Methods

    /**
     * Constructor
     *
     * @param name  the testCase name
     * @param report the test report
     * @param parameters the test parameters
     */
    protected TestCaseWithContext(@NotNull String name, @NotNull TestCaseReport.TestReport report, TestParameters.Parameter parameters) {
        this.name = name;
        this.report = report;
        this.parameters = parameters;
    }


    // region Statement functions
    public GivenCtxStmt<T, R> given(String message, CtxConsumer<R, TestCaseContext<T, R>.GCtx> fn) {
        return runIfOpen(() -> {
            this.givenMsgs.add(StmtMsg.given(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.given(message));
            this.givenFns.add(fn);
            return new GivenCtxStmt<>(this);
        });
    }


    protected void andGiven(String message, CtxConsumer<R, TestCaseContext<T, R>.GCtx> fn) {
        this.givenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.givenFns.add(fn);
    }

    public WhenCtxStmt<T, R> when(String message, CtxConsumer<R, TestCaseContext<T, R>.WCtx> fn) {
        return runIfOpen(() -> {
            this.whenMsgs.add(StmtMsg.when(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
            this.whenFns.add(fn);
            return new WhenCtxStmt<>(this);
        });
    }

    public WhenCtxStmt<T, R> whenr(String message, CtxConsumer<R, TestCaseContext<T, R>.WCtx> fn) {
        this.whenMsgs.add(StmtMsg.when(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
        this.whenFns.add(fn);
        return new WhenCtxStmt<>(this);
    }

    protected ThenCtxStmt<T, R> then(String message, ResCtxConsumer<R> fn) {
        this.thenMsgs.add(StmtMsg.then(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.then(message));
        this.thenFns.add(fn);
        return new ThenCtxStmt<>(this);
    }

    protected void andThen(String message, ResCtxConsumer<R> fn) {
        thenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.thenFns.add(fn);
    }

    // endregion

    /**
     * Print report and run the test case
     */

    protected void run() {

        System.out.print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters));

        this.givenFns.forEach(fn -> fn.accept(this.gCtx));
        this.wCtx = this.gCtx.toWCtx();
        this.whenFns.forEach(fn -> fn.accept(this.wCtx));
        this.tCtx = this.wCtx.toTCtx();
        this.thenFns.forEach(fn -> fn.accept(this.tCtx, this.tCtx.getResult()));

    }

    protected String getName(){
        if (parameters != null) return parameters.formatName(name);
        else return name;
    }

    // endregion
}
