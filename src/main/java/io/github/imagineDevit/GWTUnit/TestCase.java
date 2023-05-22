package io.github.imagineDevit.GWTUnit;


import io.github.imagineDevit.GWTUnit.report.TestCaseReport;
import io.github.imagineDevit.GWTUnit.statements.StmtMsg;
import io.github.imagineDevit.GWTUnit.statements.functions.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.imagineDevit.GWTUnit.utils.TextUtils.*;

/**
 * Test case representation
 *
 * @param <T> test case state type
 * @param <R> test case result type
 */
public class TestCase<T, R> {

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

    public static class TestCaseError extends RuntimeException {
        public TestCaseError(String message){
            super(message);
        }
    }

    /**
     * Test case result that can be either success or failure
     */
    public enum Result {
        SUCCESS("✅", green("Passed")),
        FAILURE("❌", red("Failed"));

        private final String s;
        private final String m;

        Result(String s, String m) {
            this.s = s;
            this.m = m;
        }

        public String message(String reason) {
            var r = (reason != null) ? " (reason: " + yellow(reason) + ")" : "";

            return """
                    %s
                    %s
                    """.formatted(
                    bold("RESULT: ") + s + m + r,
                    TestCase.DASH
            );
        }
    }

    /**
     * Constants
     */
    public static final String DASH = "----------";

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

    /**
     * Test case closed flag
     */
    private boolean closed = false;

    /**
     * Fns
     */
    private GivenSFn<T> givenFn = null;
    private GivenRFn givenRFn = null;
    private WhenSFn<R> whenFn = null;
    private WhenRFn whenRFn = null;
    private final List<GivenFFn<T>> andGivenFns = new ArrayList<>();
    private final List<Object> whenFns = new ArrayList<>();
   // private final List<WhenRFn> whenRFns = new ArrayList<>();
    private final List<ThenFn<R>> thenFns = new ArrayList<>();

    /**
     * Messages
     */
    private final List<StmtMsg> givenMsgs = new ArrayList<>();
    private final List<StmtMsg> whenMsgs = new ArrayList<>();
    private final List<StmtMsg> thenMsgs = new ArrayList<>();

    private final TestParameters.Parameter parameters;

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
     * @return the test case
     */
    protected static <T, R> TestCase<T, R> create(String name, TestCaseReport.TestReport report) {
        return new TestCase<>(name, report, null);
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

    public GivenStmt<T, R> given(String message, GivenSFn<T> fn) {
        return runIfOpen(() -> {
            this.givenMsgs.add(StmtMsg.given(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.given(message));
            this.givenFn = fn;
            return new GivenStmt<>(this);
        });
    }

    public GivenStmt<T, R> given(String message, GivenRFn fn) {
        return runIfOpen(() -> {
            this.givenMsgs.add(StmtMsg.given(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.given(message));
            this.givenRFn = fn;
            return new GivenStmt<>(this);
        });
    }

    protected void andGiven(String message, GivenFFn<T> fn) {
        this.givenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.andGivenFns.add(fn);
    }

    public WhenStmt<T, R> when(String message, WhenSFn<R> fn) {
        return runIfOpen(() -> {
            this.whenMsgs.add(StmtMsg.when(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
            this.whenFn = fn;
            return new WhenStmt<>(this);
        });
    }

    public WhenStmt<T, R> when(String message, WhenRFn fn) {
        return runIfOpen(() -> {
            this.whenMsgs.add(StmtMsg.when(message));
            this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
            this.whenRFn = fn;
            return new WhenStmt<>(this);
        });
    }

    protected WhenStmt<T, R> whenr(String message, WhenRFn fn) {
        this.whenMsgs.add(StmtMsg.when(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
        this.whenFns.add(fn);
        return new WhenStmt<>(this);

    }

    protected WhenStmt<T, R> when(String message, WhenFFn<T, R> fn) {
        this.whenMsgs.add(StmtMsg.when(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.when(message));
        this.whenFns.add(fn);
        return new WhenStmt<>(this);
    }

    protected ThenStmt<T, R> then(String message, ThenFn<R> fn) {
        this.thenMsgs.add(StmtMsg.then(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.then(message));
        this.thenFns.add(fn);
        return new ThenStmt<>(this);
    }

    protected void andThen(String message, ThenFn<R> fn) {
        thenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.thenFns.add(fn);
    }

    /**
     * Print report and run the test case
     */

    @SuppressWarnings("unchecked")
    protected void run() {

        System.out.print(report());

        if (this.givenFn != null) {
            this.state = TestCaseState.of(this.givenFn.get());
        } else if (this.givenRFn != null) {
            this.givenRFn.run();
        }

        this.andGivenFns.forEach(f -> this.state = state.map(f));

        if (this.whenFn != null) {
            this.result = TestCaseResult.of(this.whenFn.get());
        } else if (this.whenRFn != null) {
            this.whenRFn.run();
        } else {


            this.whenFns.forEach(fn -> {
                if (fn instanceof WhenFFn<?,?> gfn) {
                    this.result = TestCaseResult.of(((WhenFFn<T,R>) gfn).apply(this.state));
                } else if (fn instanceof WhenRFn rfn) {
                    rfn.run();
                }
            });
        }

        this.thenFns.forEach(fn -> fn.accept(this.result));

    }

    protected String getName(){
        if (parameters != null) return parameters.formatName(name);
        else return name;
    }

    private String report() {

        var n = name;
        if (parameters != null){
            n = parameters.formatName(n);
        }
        var title = bold("TEST CASE") + ": " + italic(purple(n));

        String givenMsg = givenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));
        String whenMsg = whenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));
        String thenMsg = thenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));

        return """
                              
                %s
                %s
                %s
                %s
                %s
                %s
                %s
                  """.formatted(
                DASH, title, DASH,
                givenMsg,
                whenMsg,
                thenMsg,
                DASH
        );
    }

    private <S> S runIfOpen(Supplier<S> fn) {
        if (this.closed) {
            throw new IllegalStateException("""
                                        
                    Test case is already closed.
                    A test case can only be run once.
                    """);
        }
        this.closed = true;
        return fn.get();
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> WhenSFn<E> catchItFn(Class<E> exceptionClazz, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e){
            if (e.getClass() == exceptionClazz) {
                return () -> (E) e;
            }
            throw new TestCaseError("Expected <%s> but found <%s> ".formatted(exceptionClazz.getName(), e.getClass().getName()));
        }

        throw new TestCaseError("Expected <%s> exception to be thrown, but no exception thrown ".formatted(exceptionClazz.getName()));
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> E catchIt(Class<E> exceptionClazz, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e){
            if (e.getClass() == exceptionClazz) {
                return (E) e;
            }
            throw new TestCaseError("Expected <%s> but found <%s> ".formatted(exceptionClazz.getName(), e.getClass().getName()));
        }

        throw new TestCaseError("Expected <%s> exception to be thrown, but no exception thrown ".formatted(exceptionClazz.getName()));
    }

}
