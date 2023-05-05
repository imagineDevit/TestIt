package io.github.imaginedevit.testIt;


import io.github.imaginedevit.testIt.casefns.AndGivenFn;
import io.github.imaginedevit.testIt.casefns.GWhenFn;

public class GivenStmt<T, R> {

    private final TestCase<T,R> testCase;

    public GivenStmt(TestCase<T, R> testCase) {
        this.testCase = testCase;
    }

    public  GivenStmt<T,R> and(String message, AndGivenFn<T> fn) {
        testCase.andGiven(message, fn);
        return this;
    }

    public  WhenStmt<T,R> when(String message, GWhenFn<T,R> fn) {
        return testCase.gWhen(message, fn);
    }

}
