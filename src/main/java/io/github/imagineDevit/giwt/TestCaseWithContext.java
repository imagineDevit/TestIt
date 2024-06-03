package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.core.ATestCase;
import io.github.imagineDevit.giwt.core.TestParameters;
import io.github.imagineDevit.giwt.core.report.TestCaseReport;
import io.github.imagineDevit.giwt.core.utils.Utils;
import io.github.imagineDevit.giwt.statements.functions.context.AGCtxFn;
import io.github.imagineDevit.giwt.statements.functions.context.GCtxFn;
import io.github.imagineDevit.giwt.statements.functions.context.TCtxFn;
import io.github.imagineDevit.giwt.statements.functions.context.WCtxFns;

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
    private final List<AGCtxFn<T, R>> aGivenFns = new ArrayList<>();
    private final List<TCtxFn<T, R>> thenFns = new ArrayList<>();
    private GCtxFn<T, R> givenFn = null;
    private WCtxFns<T, R> whenFn;

    protected TestCaseWithContext(String name, TestCaseReport.TestReport report, TestParameters.Parameter parameters) {
        super(name, report, parameters);
    }

    public GivenCtxStmt<T, R> given(String message, GCtxFn<T, R> fn) {
        return runIfOpen(() -> {
            this.addGivenMsg(message);
            this.givenFn = fn;
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

    protected void andGiven(String message, AGCtxFn<T, R> fn) {
        this.addAndGivenMsg(message);
        this.aGivenFns.add(fn);
    }

    public WhenCtxStmt<T, R> when(String message, WCtxFns.WCtxFFn<T, R> fn) {
        return runIfOpen(() -> {
            this.addWhenMsg(message);
            this.whenFn = fn;
            return new WhenCtxStmt<>(this);
        });
    }

    protected WhenCtxStmt<T, R> whenr(String message, WCtxFns.WCtxSFn<T, R> fn) {
        this.addWhenMsg(message);
        this.whenFn = fn;
        return new WhenCtxStmt<>(this);
    }

    protected ThenCtxStmt<T, R> then(String message, TCtxFn<T, R> fn) {
        this.addThenMsg(message);
        this.thenFns.add(fn);
        return new ThenCtxStmt<>(this);
    }

    protected void andThen(String message, TCtxFn<T, R> fn) {
        this.addAndThenMsg(message);
        this.thenFns.add(fn);
    }

    @Override
    protected void run() {

        System.out.print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters));

        TestCaseContext.AGCtx<T, R> aGctx;
        TestCaseContext.WCtx<T, R> wctx;

        if (this.givenFn != null) {
            this.ctx.setState(this.givenFn.apply(this.ctx));
        }

        if (!this.aGivenFns.isEmpty()) {
            aGctx = this.ctx.toAGCtx();
            this.aGivenFns.forEach(fn -> fn.accept(aGctx, aGctx.getState().value()));
            wctx = aGctx.toWCtx();
        } else {
            wctx = this.ctx.toWCtx();
        }


        try {

            if (this.whenFn instanceof WCtxFns.WCtxFFn<T, R> fn) {
                fn.accept(wctx);
            } else if (this.whenFn instanceof WCtxFns.WCtxSFn<T, R> fn) {
                wctx.setResult(TestCaseCtxResult.of(fn.apply(wctx, wctx.getState().value())));
            }
        } catch (Exception e) {
            wctx.setResult(TestCaseCtxResult.ofErr(e));
        }

        System.out.print(Utils.listExpectations());
        var tctx = wctx.toTCtx();
        this.thenFns.forEach(fn -> fn.accept(tctx, tctx.getResult()));

        System.out.println();
    }

    public record GivenCtxStmt<T, R>(TestCaseWithContext<T, R> testCase) {

        public GivenCtxStmt<T, R> and(String message, AGCtxFn<T, R> fn) {
            testCase.andGiven(message, fn);
            return this;
        }

        public WhenCtxStmt<T, R> when(String message, WCtxFns.WCtxSFn<T, R> fn) {
            return testCase.whenr(message, fn);
        }

    }

    public record WhenCtxStmt<T, R>(TestCaseWithContext<T, R> testCase) {

        public ThenCtxStmt<T, R> then(String message, TCtxFn<T, R> fn) {
            return testCase.then(message, fn);
        }

    }

    public record ThenCtxStmt<T, R>(TestCaseWithContext<T, R> testCase) {

        public ThenCtxStmt<T, R> and(String message, TCtxFn<T, R> fn) {
            testCase.andThen(message, fn);
            return this;
        }

    }

}
