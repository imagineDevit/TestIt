package io.github.imagineDevit.GWTUnit;


import io.github.imagineDevit.GWTUnit.report.TestCaseReport;
import io.github.imagineDevit.GWTUnit.statements.StmtMsg;
import io.github.imagineDevit.GWTUnit.statements.functions.*;
import io.github.imagineDevit.GWTUnit.utils.TextUtils;
import io.github.imagineDevit.GWTUnit.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case representation
 *
 * @param <T> test case state type
 * @param <R> test case result type
 */
public class TestCase<T, R> extends CloseableCase {

    // region Statement classes
    public static class GivenStmt<T, R> {

        private final TestCase<T,R> testCase;

        public GivenStmt(TestCase<T, R> testCase) {
            this.testCase = testCase;
        }

        public GivenStmt<T,R> and(String message, GivenFFn<T> fn) {
            testCase.andGiven(message, fn);
            return this;
        }

        public WhenStmt<T,R> when(String message, WhenFFn<T,R> fn) {
            return testCase.when(message, fn);
        }

        public  WhenStmt<T,R> when(String message, WhenRFn fn) {
            return testCase.whenr(message, fn);
        }

    }

    public static class WhenStmt<T,R> {

        private final TestCase<T,R> testCase;

        public WhenStmt(TestCase<T, R> testCase) {
            this.testCase = testCase;
        }

        public ThenStmt<T,R> then(String message, ThenFn<R> fn) {
            return testCase.then(message, fn);
        }

    }

    public static class ThenStmt<T,R> {

        private final TestCase<T,R> testCase;

        public ThenStmt(TestCase<T, R> testCase) {
            this.testCase = testCase;
        }

        public ThenStmt<T,R> and(String message, ThenFn<R> fn) {
            testCase.andThen(message, fn);
            return this;
        }

    }

    /**
     * Test case result that can be either success or failure
     */
    public enum Result {
        SUCCESS("✅", TextUtils.green("Passed")),
        FAILURE("❌", TextUtils.red("Failed"));

        private final String s;
        private final String m;

        Result(String s, String m) {
            this.s = s;
            this.m = m;
        }

        public String message(String reason) {
            var r = (reason != null) ? " (reason: " + TextUtils.yellow(reason) + ")" : "";

            return """
                    %s
                    %s
                    """.formatted(
                    TextUtils.bold("RESULT: ") + s + m + r,
                    TestCase.DASH
            );
        }
    }

    // endregion

    // region Constants
    public static final String DASH = "----------";

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

    /**
     * Test case state
     */
    private TestCaseState<T> state = TestCaseState.empty();

    /**
     * Test case result
     */
    private TestCaseResult<R> result = TestCaseResult.empty();



    private TestCaseWithContext<R> ctxCase = null;

    /**
     * Fns
     */
    private GivenSFn<T> givenFn = null;
    private GivenRFn givenRFn = null;
    private WhenSFn<R> whenFn = null;
    private WhenRFn whenRFn = null;
    private final List<GivenFFn<T>> andGivenFns = new ArrayList<>();
    private final List<Object> whenFns = new ArrayList<>();
    private final List<ThenFn<R>> thenFns = new ArrayList<>();

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
    private TestCase(@NotNull String name, @NotNull TestCaseReport.TestReport report, TestParameters.Parameter parameters) {
        this.name = name;
        this.report = report;
        this.parameters = parameters;
    }

    /**
     * Create a test case
     *
     * @param name test case name
     * @param report test case report
     * @param parameters test case parameters
     * @return the test case
     */
    protected static <T, R> TestCase<T, R> create(String name, TestCaseReport.TestReport report, TestParameters.Parameter parameters) {
        return new TestCase<>(name, report, parameters);
    }

    /**
     * Create a new test case with a context
     *
     * @return the test case with a context
     */
    public TestCaseWithContext<R> withContext() {
        this.ctxCase =  new TestCaseWithContext<>(this.name, this.report, this.parameters);
        return ctxCase;
    }

    // region Statement functions

    /**
     * Creates a new Given statement with the provided message and (supplier) function.
     *
     * @param message the description of the statement being given
     * @param fn the function that executes the given statement
     * @return the Given statement object
     */
    public GivenStmt<T, R> given(String message, GivenSFn<T> fn) {
        return runIfOpen(() -> {
            this.givenMsgs.add(StmtMsg.given(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.given(message));
            this.givenFn = fn;
            return new GivenStmt<>(this);
        });
    }

    /**
     * Creates a new Given statement with the provided message and (runnable) function.
     *
     * @param message the description of the statement being given
     * @param fn the function that executes the given statement
     * @return the Given statement object
     */
    public GivenStmt<T, R> given(String message, GivenRFn fn) {
        return runIfOpen(() -> {
            this.givenMsgs.add(StmtMsg.given(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.given(message));
            this.givenRFn = fn;
            return new GivenStmt<>(this);
        });
    }

    /**
     * Adds another Given statement to the current test case with the provided message and function.
     *
     * @param message the description of the new Given statement
     * @param fn the function that executes the given statement
     */
    protected void andGiven(String message, GivenFFn<T> fn) {
        this.givenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.andGivenFns.add(fn);
    }

    /**
     * Adds a When statement to the current test case with the provided message and a supplier function.
     *
     * @param message the description of the new When statement
     * @param fn the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    public WhenStmt<T, R> when(String message, WhenSFn<R> fn) {
        return runIfOpen(() -> {
            this.whenMsgs.add(StmtMsg.when(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
            this.whenFn = fn;
            return new WhenStmt<>(this);
        });
    }

    /**
     * Adds a When statement to the current test case with the provided message and a runnable function
     *
     * @param message the description of the new When statement
     * @param fn the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    public WhenStmt<T, R> when(String message, WhenRFn fn) {
        return runIfOpen(() -> {
            this.whenMsgs.add(StmtMsg.when(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
            this.whenRFn = fn;
            return new WhenStmt<>(this);
        });
    }

    /**
     * Adds a When statement to the current test case with the provided message and a runnable function
     *
     * @param message the description of the new When statement
     * @param fn the function that executes the When statement
     * @return a new instance of WhenStmt that is associated with this test case
     */
    protected WhenStmt<T, R> whenr(String message, WhenRFn fn) {
        this.whenMsgs.add(StmtMsg.when(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
        this.whenFns.add(fn);
        return new WhenStmt<>(this);

    }

    /**
     * Adds a When statement to the current test case with the provided message and a function.
     *
     * @param message the description of the new When statement
     * @param fn the function that executes the When statement and returns a result of type R
     * @return a new instance of WhenStmt that is associated with this test case
     */
    protected WhenStmt<T, R> when(String message, WhenFFn<T, R> fn) {
        this.whenMsgs.add(StmtMsg.when(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
        this.whenFns.add(fn);
        return new WhenStmt<>(this);
    }

    /**
     * Adds a Then statement to the current test case with the provided message and a consumer function.
     *
     * @param message the description of the new Then statement
     * @param fn the function that executes the Then statement
     * @return a new instance of ThenStmt that is associated with this test case
     */
    protected ThenStmt<T, R> then(String message, ThenFn<R> fn) {
        this.thenMsgs.add(StmtMsg.then(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.then(message));
        this.thenFns.add(fn);
        return new ThenStmt<>(this);
    }

    /**
     * Adds a then statement to the current test case with the provided message and a consumer function.
     * This method is used in conjunction with the initial ThenStmt method, allowing for multiple Then statements in a single test case.
     *
     * @param message the description of the new Then statement
     * @param fn the function that executes the additional Then statement
     */
    protected void andThen(String message, ThenFn<R> fn) {
        thenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.thenFns.add(fn);
    }

    // endregion


    /**
     * Runs the test case and performs the given, when, and then actions, and generates a report.
     * If the test case has a context, it will run the context testCase instead.
     * Note: This method is protected, which means it can only be accessed by classes within the same package or subclasses.
     */
    @SuppressWarnings("unchecked")
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

        this.andGivenFns.forEach(f -> this.state = f.apply(this.state));

        try {
            if (this.whenFn != null) {
                this.result = TestCaseResult.of(this.whenFn.get());
            } else if (this.whenRFn != null) {
                this.whenRFn.run();
            } else {
                this.whenFns.forEach(fn -> {
                    if (fn instanceof WhenFFn<?,?> gfn) {
                        this.result = ((WhenFFn<T,R>) gfn).apply(this.state);
                    } else if (fn instanceof WhenRFn rfn) {
                        rfn.run();
                    }
                });
            }
        } catch (Exception e) {
            this.result.withError(e);
        }

        this.thenFns.forEach(fn -> fn.accept(this.result));

    }

    /**
     * Returns the name of the test case with its relevant parameters formatted.
     * If the test case has parameters, it will format the name using the parameters.
     * If not, it will return the name as is.
     * Note: This method is protected, which means it can only be accessed by classes within the same package or subclasses.
     *
     * @return the formatted name of the test case.
     */
    protected String getName(){
        if (parameters != null) return parameters.formatName(name);
        else return name;
    }

    // endregion
}
