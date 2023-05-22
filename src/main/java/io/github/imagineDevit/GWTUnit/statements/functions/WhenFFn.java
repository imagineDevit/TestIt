package io.github.imagineDevit.GWTUnit.statements.functions;

import io.github.imagineDevit.GWTUnit.TestCaseState;

import java.util.function.Function;


public interface WhenFFn<T,R> extends Function<TestCaseState<T>,R> {}

