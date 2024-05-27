package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCase;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.report.TestCaseReport;
import io.github.imagineDevit.giwt.core.utils.Utils;
import io.github.imagineDevit.giwt.statements.functions.context.CtxConsumer;
import io.github.imagineDevit.giwt.statements.functions.context.ResCtxConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case representation with context
 *
 * @param <R> test case result type
 * @author Henri Joel SEDJAME
 * @since 0.1.0
 */
public class TestCaseWithContext<T, R> extends ATestCase<T, R, TestCaseCtxState<T>, TestCaseCtxResult<R>> {

    private final TestCaseContext.GCtx<T, R> ctx = new TestCaseContext.GCtx<>();
    private CtxConsumer<R, TestCaseContext.GCtx<T, R>> givenFn = null;
    private final List<CtxConsumer<R, TestCaseContext.AGCtx<T, R>>> aGivenFns = new ArrayList<>();
    private final List<ResCtxConsumer<T, R>> thenFns = new ArrayList<>();
    private CtxConsumer<R, TestCaseContext.WCtx<T, R>> whenFn;

    protected TestCaseWithContext(String name, TestCaseReport.TestReport report, TestParameters.Parameter parameters) {
        super(name, report, parameters);
    }

    public GivenCtxStmt<T, R> given(String message, CtxConsumer<R, TestCaseContext.GCtx<T, R>> fn) {
        return runIfOpen(() -> {
            this.addGivenMsg(message);
            this.givenFn  = fn;
            return new GivenCtxStmt<>(this);
        });
    }

    public GivenCtxStmt<T, R> given(String message, T t) {
        return runIfOpen(() -> {
            this.addGivenMsg(message);
            this.ctx.setState(t);
            return new GivenCtxStmt<>(this);
        });
    }

    protected void andGiven(String message, CtxConsumer<R, TestCaseContext.AGCtx<T, R>> fn) {
        this.addAndGivenMsg(message);
        this.aGivenFns.add(fn);
    }

    public WhenCtxStmt<T, R> when(String message, CtxConsumer<R, TestCaseContext.WCtx<T, R>> fn) {
        return runIfOpen(() -> {
            this.addWhenMsg(message);
            this.whenFn = fn;
            return new WhenCtxStmt<>(this);
        });
    }

    protected WhenCtxStmt<T, R> whenr(String message, CtxConsumer<R, TestCaseContext.WCtx<T, R>> fn) {
        this.addWhenMsg(message);
        this.whenFn = fn;
        return new WhenCtxStmt<>(this);
    }

    protected ThenCtxStmt<T, R> then(String message, ResCtxConsumer<T, R> fn) {
        this.addThenMsg(message);
        this.thenFns.add(fn);
        return new ThenCtxStmt<>(this);
    }

    protected void andThen(String message, ResCtxConsumer<T, R> fn) {
        this.addAndThenMsg(message);
        this.thenFns.add(fn);
    }

    @Override
    protected void run() {

        System.out.print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters));

        TestCaseContext.AGCtx<T,R> aGctx;
        TestCaseContext.WCtx<T,R> wctx;

        if (this.givenFn != null) {
            this.givenFn.accept(this.ctx);
        }

        if (!this.aGivenFns.isEmpty()) {
            aGctx = this.ctx.toAGCtx();
            this.aGivenFns.forEach(fn -> fn.accept(aGctx));
            wctx = aGctx.toWCtx();
        } else {
            wctx = this.ctx.toWCtx();
        }


        try {
            this.whenFn.accept(wctx);
        } catch (Exception e) {
            wctx.setResult(TestCaseCtxResult.ofErr(e));
        }
        var tctx = wctx.toTCtx();
        this.thenFns.forEach(fn -> fn.accept(tctx, tctx.getResult()));

    }

    public record GivenCtxStmt<T, R>(TestCaseWithContext<T, R> testCase) {

        public GivenCtxStmt<T, R> and(String message, CtxConsumer<R, TestCaseContext.AGCtx<T, R>> fn) {
            testCase.andGiven(message, fn);
            return this;
        }

        public WhenCtxStmt<T, R> when(String message, CtxConsumer<R, TestCaseContext.WCtx<T, R>> fn) {
            return testCase.whenr(message, fn);
        }

    }

    public record WhenCtxStmt<T, R>(TestCaseWithContext<T, R> testCase) {

        public ThenCtxStmt<T, R> then(String message, ResCtxConsumer<T, R> fn) {
            return testCase.then(message, fn);
        }

    }

    public record ThenCtxStmt<T, R>(TestCaseWithContext<T, R> testCase) {

        public ThenCtxStmt<T, R> and(String message, ResCtxConsumer<T, R> fn) {
            testCase.andThen(message, fn);
            return this;
        }

    }

}
