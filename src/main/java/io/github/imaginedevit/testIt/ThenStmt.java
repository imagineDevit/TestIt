package io.github.imaginedevit.testIt;

import io.github.imaginedevit.testIt.casefns.ThenFn;

public class ThenStmt<T,R> {

    private final TestCase<T,R> testCase;

    public ThenStmt(TestCase<T, R> testCase) {
        this.testCase = testCase;
    }

    public ThenStmt<T,R> and(String message, ThenFn<R> fn) {
        testCase.andThen(message, fn);
        return this;
    }

}
