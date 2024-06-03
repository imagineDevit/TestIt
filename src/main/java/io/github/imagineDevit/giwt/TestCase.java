package io.github.imagineDevit.giwt;


import io.github.imagineDevit.giwt.core.ATestCase;
import io.github.imagineDevit.giwt.core.report.TestCaseReport;
import io.github.imagineDevit.giwt.core.utils.Utils;
import io.github.imagineDevit.giwt.statements.functions.givens.AndGivenFn;
import io.github.imagineDevit.giwt.statements.functions.givens.GivenRFn;
import io.github.imagineDevit.giwt.statements.functions.givens.GivenSFn;
import io.github.imagineDevit.giwt.statements.functions.thens.ThenFn;
import io.github.imagineDevit.giwt.statements.functions.whens.WhenFn;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case representation
 *
 * @param <T> test case state type
 * @param <R> test case result type
 */
@SuppressWarnings({"unchecked", "unused", "unusedReturnValue"})
public class TestCase<T, R> extends ATestCase<T, R, TestCaseState<T>, TestCaseResult<R>> {


    private final List<AndGivenFn<T>> andGivenFns = new ArrayList<>();
    private final List<ThenFn<R>> thenFns = new ArrayList<>();
    private GivenSFn<T> givenFn = null;
    private GivenRFn givenRFn = null;
    private WhenFn whenFn = null;
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

    /**
     * Creates a new Given statement with the provided message and (supplier) function.
     *
     * @param message the description of the statement being given
     * @param fn      the function that executes the given statement
     * @return the Given statement object
     */
    public GivenStmt<T, R> given(String message, GivenSFn<T> fn) {
        return runIfOpen(() -> {
            this.addGivenMsg(message);
            this.givenFn = fn;
            return new GivenStmt<>(this);
        });
    }

    /**
     * Creates a new Given statement with the provided message and value.
     *
     * @param message the description of the statement being given
     * @param t       the value to be used in the given statement
     * @return the Given statement object
     */
    public GivenStmt<T, R> given(String message, T t) {
        return runIfOpen(() -> {
            this.addGivenMsg(message);
            this.state = TestCaseState.of(t);
            return new GivenStmt<>(this);
        });
    }

    /**
     * Creates a new Given statement with the provided message and (runnable) function.
     *
     * @param message the description of the statement being given
     * @param fn      the function that executes the given statement
     * @return the Given statement object
     */
    public GivenStmt<T, R> given(String message, GivenRFn fn) {
        return runIfOpen(() -> {
            this.addGivenMsg(message);
            this.givenRFn = fn;
            return new GivenStmt<>(this);
        });
    }

    /**
     * Adds another Given statement to the current test case with the provided message and function.
     *
     * @param message the description of the new Given statement
     * @param fn      the function that executes the given statement
     */
    protected void andGiven(String message, AndGivenFn<T> fn) {
        this.addAndGivenMsg(message);
        this.andGivenFns.add(fn);
    }

    /**
     * Adds a When statement to the current test case with the provided message and a supplier function.
     *
     * @param message the description of the new When statement
     * @param fn      the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    public WhenStmt<T, R> when(String message, WhenFn.S<R> fn) {
        return runIfOpen(() -> whens(message, fn));
    }

    /**
     * Adds a When statement to the current test case with the provided message and a runnable function
     *
     * @param message the description of the new When statement
     * @param fn      the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    public WhenStmt<T, R> when(String message, WhenFn.R fn) {
        return runIfOpen(() -> this.whenr(message, fn));
    }

    /**
     * Adds a When statement to the current test case with the provided message and a consumer function
     *
     * @param message the description of the new When statement
     * @param fn      the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    protected WhenStmt<T, R> whenc(String message, WhenFn.C<T> fn) {
        this.addWhenMsg(message);
        this.whenFn = fn;
        return new WhenStmt<>(this);
    }

    /**
     * Adds a When statement to the current test case with the provided message and a runnable function
     *
     * @param message the description of the new When statement
     * @param fn      the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    protected WhenStmt<T, R> whenr(String message, WhenFn.R fn) {
        this.addWhenMsg(message);
        this.whenFn = fn;
        return new WhenStmt<>(this);
    }

    /**
     * Adds a When statement to the current test case with the provided message and a runnable function
     *
     * @param message the description of the new When statement
     * @param fn      the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    protected WhenStmt<T, R> whens(String message, WhenFn.S<R> fn) {
        this.addWhenMsg(message);
        this.whenFn = fn;
        return new WhenStmt<>(this);
    }

    /**
     * Adds a When statement to the current test case with the provided message and a function.
     *
     * @param message the description of the new When statement
     * @param fn      the function that executes the When statement and returns a result of type R
     * @return a new instance of WhenStmt that is associated with this test case
     */
    protected WhenStmt<T, R> when(String message, WhenFn.F<T, R> fn) {
        this.addWhenMsg(message);
        this.whenFn = fn;
        return new WhenStmt<>(this);
    }


    /**
     * Adds a Then statement to the current test case with the provided message and a consumer function.
     *
     * @param message the description of the new Then statement
     * @param fn      the function that executes the Then statement
     * @return a new instance of ThenStmt that is associated with this test case
     */
    protected ThenStmt<T, R> then(String message, ThenFn<R> fn) {
        this.addThenMsg(message);
        this.thenFns.add(fn);
        return new ThenStmt<>(this);
    }

    /**
     * Adds a then statement to the current test case with the provided message and a consumer function.
     * This method is used in conjunction with the initial ThenStmt method, allowing for multiple Then statements in a single test case.
     *
     * @param message the description of the new Then statement
     * @param fn      the function that executes the additional Then statement
     */
    protected void andThen(String message, ThenFn<R> fn) {
        this.addAndThenMsg(message);
        this.thenFns.add(fn);
    }

    @Override
    protected void run() {

        if (ctxCase != null) {
            ctxCase.run();
            return;
        }

        System.out.print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters));

        if (this.givenFn != null) {
            this.state = TestCaseState.of(this.givenFn.get());
        } else if (this.givenRFn != null) {
            this.givenRFn.run();
        }

        this.andGivenFns.forEach(this.state::consumeValue);

        try {
            if (this.whenFn != null) {
                if (this.whenFn instanceof WhenFn.F<?, ?> gfn) {
                    this.result = TestCaseResult.of(((WhenFn.F<T, R>) gfn).apply(this.state.value()));
                } else if (this.whenFn instanceof WhenFn.R rfn) {
                    rfn.run();
                } else if (this.whenFn instanceof WhenFn.C<?> cfn) {
                    this.state.consumeValue((WhenFn.C<T>) cfn);
                } else if (this.whenFn instanceof WhenFn.S<?> sfn) {
                    this.result = TestCaseResult.of(((WhenFn.S<R>) sfn).get());
                }
            }
        } catch (Throwable e) {
            this.result = TestCaseResult.ofErr(e);
        }

        System.out.print(Utils.listExpectations());

        this.thenFns.forEach(fn -> fn.accept(this.result));
        System.out.println();
    }

    public record GivenStmt<T, R>(TestCase<T, R> testCase) {

        public GivenStmt<T, R> and(String message, AndGivenFn<T> fn) {
            testCase.andGiven(message, fn);
            return this;
        }

        public WhenStmt<T, R> when(String message, WhenFn.F<T, R> fn) {
            return testCase.when(message, fn);
        }

        public WhenStmt<T, R> when(String message, WhenFn.C<T> fn) {
            return testCase.whenc(message, fn);
        }

        public WhenStmt<T, R> when(String message, WhenFn.R fn) {
            return testCase.whenr(message, fn);
        }

        public WhenStmt<T, R> when(String message, WhenFn.S<R> fn) {
            return testCase.whens(message, fn);
        }

    }

    public record WhenStmt<T, R>(TestCase<T, R> testCase) {

        public ThenStmt<T, R> then(String message, ThenFn<R> fn) {
            return testCase.then(message, fn);
        }

    }

    public record ThenStmt<T, R>(TestCase<T, R> testCase) {

        public ThenStmt<T, R> and(String message, ThenFn<R> fn) {
            testCase.andThen(message, fn);
            return this;
        }

    }

}
