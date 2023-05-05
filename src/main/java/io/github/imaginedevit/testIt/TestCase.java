package io.github.imaginedevit.testIt;


import io.github.imaginedevit.testIt.casefns.*;
import io.github.imaginedevit.testIt.utils.StmtMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.imaginedevit.testIt.utils.TextUtils.*;

/**
 * Test case representation
 *
 * @param <T> test case state type
 * @param <R> test case result type
 */
public class TestCase<T, R> {

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
    public static final String RUN = "run";
    public static final String DASH = "----------";

    private final String name;
    private T state;
    private R result;

    private boolean closed = false;


    /**
     * Fns
     */
    private GivenFn<T> givenFn = null;
    private WhenFn<R> whenFn = null;
    private final List<AndGivenFn<T>> andGivenFns = new ArrayList<>();
    private final List<GWhenFn<T,R>> whenFns = new ArrayList<>();
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
    private TestCase(String name) {
        this.name = name;
    }

    /**
     * Create method
     *
     * @param name test case name
     * @return the test case
     */
    public static <T, R> TestCase<T, R> create(String name) {
        return new TestCase<>(name);
    }

    public  GivenStmt<T, R> given(String message, GivenFn<T> fn) {
        return runIfOpen(()->{
            this.givenMsgs.add(StmtMsg.given(message));
            this.givenFn = fn;
            return new GivenStmt<>(this);
        });
    }

    protected void andGiven(String message, AndGivenFn<T> fn) {
        this.givenMsgs.add(StmtMsg.and(message));
        this.andGivenFns.add(fn);
    }

    public  WhenStmt<T, R> when(String message, WhenFn<R> fn) {
        return runIfOpen(() -> {
            this.whenMsgs.add(StmtMsg.when(message));
            this.whenFn = fn;
            return new WhenStmt<>(this);
        });
    }

    protected  WhenStmt<T, R> gWhen(String message, GWhenFn<T,R> fn) {
        this.whenMsgs.add(StmtMsg.when(message));
        this.whenFns.add(fn);
        return new WhenStmt<>(this);
    }

    protected ThenStmt<T, R> then(String message, ThenFn<R> fn) {
        this.thenMsgs.add(StmtMsg.then(message));
        this.thenFns.add(fn);
        return new ThenStmt<>(this);
    }

    protected void andThen(String message, ThenFn<R> fn) {
        thenMsgs.add(StmtMsg.and(message));
        this.thenFns.add(fn);
    }

    /**
     * Print report and run the test case
     */

    protected void run() {

        System.out.print(report());
        if (this.givenFn != null) {
            this.state = this.givenFn.get();
        }

        this.andGivenFns.forEach(f -> this.state = f.apply(Optional.ofNullable(this.state)));

        if (this.whenFn != null) {
            this.result = this.whenFn.get().orElse(null);
        } else {
            this.whenFns.forEach(fn -> this.result = fn.apply(Optional.ofNullable(this.state)));
        }

        this.thenFns.forEach(fn -> fn.accept(Optional.ofNullable(this.result)));
    }

    private String report() {

        var title = bold("TEST CASE")+ ": " + italic(purple(name));

        return """
              
                %s
                %s
                %s
                %s
                %s
                %s
                %s
                  """.formatted(
                DASH,title,DASH,
                givenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n")),
                whenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n")),
                thenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n")),
                DASH
        );
    }

    private <S> S runIfOpen(Supplier<S> fn){
        if(this.closed){
            throw new IllegalStateException("""
                    
                    Test case is already closed.
                    A test case can only be run once.
                    """);
        }
        this.closed = true;
        return fn.get();
    }

}
