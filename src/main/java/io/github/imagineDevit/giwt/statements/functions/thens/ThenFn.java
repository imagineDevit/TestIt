package io.github.imagineDevit.giwt.statements.functions.thens;

import io.github.imagineDevit.giwt.TestCaseResult;

import java.util.function.Consumer;

public interface ThenFn<R> extends Consumer<TestCaseResult<R>> {
}
