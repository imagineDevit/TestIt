package io.github.imaginedevit.testIt;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.imaginedevit.testIt.TextUtils.*;

/**
 * Test case representation
 *
 * @param <T> test case state type
 * @param <R> test case result type
 */
public class TestCase<T, R> {

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

    private final TestCaseReport.TestReport report;

    /**
     * Test case state and result
     */
    private T state;
    private R result;

    /**
     * Test case closed flag
     */
    private boolean closed = false;


    /**
     * Fns
     */
    private GivenFn<T> givenFn = null;
    private GivenRFn givenRFn = null;
    private WhenFn<R> whenFn = null;
    private WhenRFn whenRFn = null;
    private final List<AndGivenFn<T>> andGivenFns = new ArrayList<>();
    private final List<Object> whenFns = new ArrayList<>();
   // private final List<WhenRFn> whenRFns = new ArrayList<>();
    private final List<ThenFn<R>> thenFns = new ArrayList<>();

    /**
     * Messages
     */
    private final List<StmtMsg> givenMsgs = new ArrayList<>();
    private final List<StmtMsg> whenMsgs = new ArrayList<>();
    private final List<StmtMsg> thenMsgs = new ArrayList<>();


    /**
     * Constructor with name
     *
     * @param name the testCase name
     */
    private TestCase(String name, TestCaseReport.TestReport report) {
        this.name = name;
        this.report = report;
    }

    /**
     * Create method
     *
     * @param name test case name
     * @return the test case
     */
    protected static <T, R> TestCase<T, R> create(String name, TestCaseReport.TestReport report) {
        return new TestCase<>(name, report);
    }

    public GivenStmt<T, R> given(String message, GivenFn<T> fn) {
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


    protected void andGiven(String message, AndGivenFn<T> fn) {
        this.givenMsgs.add(StmtMsg.and(message));
        this.report.addDescriptionItem(TestCaseReport.TestReport.DescriptionItem.and(message));
        this.andGivenFns.add(fn);
    }

    public WhenStmt<T, R> when(String message, WhenFn<R> fn) {
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

    protected WhenStmt<T, R> when(String message, GWhenFn<T, R> fn) {
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
            this.state = this.givenFn.get();
        } else if (this.givenRFn != null) {
            this.givenRFn.run();
        }

        this.andGivenFns.forEach(f -> this.state = f.apply(Optional.ofNullable(this.state)));

        if (this.whenFn != null) {
            this.result = this.whenFn.get();
        } else if (this.whenRFn != null) {
            this.whenRFn.run();
        } else {


            this.whenFns.forEach(fn -> {
                if (fn instanceof GWhenFn<?,?> gfn) {
                    this.result = ((GWhenFn<T,R>) gfn).apply(Optional.ofNullable(this.state));
                } else if (fn instanceof WhenRFn rfn) {
                    rfn.run();
                }
            });
        }

        this.thenFns.forEach(fn -> fn.accept(Optional.ofNullable(this.result)));

    }

    private String report() {

        var title = bold("TEST CASE") + ": " + italic(purple(name));

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

}
