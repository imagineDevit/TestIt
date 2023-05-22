package io.github.imagineDevit.GWTUnit.statements.functions;

import java.util.function.Function;


@FunctionalInterface
public interface GivenFFn<T> extends Function<T,T> {}
