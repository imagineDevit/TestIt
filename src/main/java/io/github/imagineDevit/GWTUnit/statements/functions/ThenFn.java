package io.github.imagineDevit.GWTUnit.statements.functions;

import io.github.imagineDevit.GWTUnit.TestCaseResult;

import java.util.function.Consumer;

public interface ThenFn<R> extends Consumer<TestCaseResult<R>> {}
