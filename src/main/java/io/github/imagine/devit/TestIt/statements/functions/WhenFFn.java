package io.github.imagine.devit.TestIt.statements.functions;

import io.github.imagine.devit.TestIt.TestCaseState;

import java.util.function.Function;


public interface WhenFFn<T,R> extends Function<TestCaseState<T>,R> {}

