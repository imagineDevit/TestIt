package io.github.imagine.devit.TestIt.statements.functions;

import io.github.imagine.devit.TestIt.TestCaseResult;

import java.util.function.Consumer;

public interface ThenFn<R> extends Consumer<TestCaseResult<R>> {}
