package io.github.imagineDevit.giwt.statements.functions;

import io.github.imagineDevit.giwt.TestCaseState;

import java.util.function.UnaryOperator;


@FunctionalInterface
public interface GivenFFn<T> extends UnaryOperator<T> {}
