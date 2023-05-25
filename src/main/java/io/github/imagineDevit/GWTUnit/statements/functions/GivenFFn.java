package io.github.imagineDevit.GWTUnit.statements.functions;

import io.github.imagineDevit.GWTUnit.TestCaseState;

import java.util.function.UnaryOperator;


@FunctionalInterface
public interface GivenFFn<T> extends UnaryOperator<TestCaseState<T>> {}
