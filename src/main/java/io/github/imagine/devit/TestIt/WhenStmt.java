package io.github.imagine.devit.TestIt;

public class WhenStmt<T,R> {

    private final TestCase<T,R> testCase;

    public WhenStmt(TestCase<T, R> testCase) {
        this.testCase = testCase;
    }


    public ThenStmt<T,R> then(String message, ThenFn<R> fn) {
        return testCase.then(message, fn);
    }

}
