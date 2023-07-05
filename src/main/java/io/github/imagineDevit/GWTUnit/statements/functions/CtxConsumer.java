package io.github.imagineDevit.GWTUnit.statements.functions;

import io.github.imagineDevit.GWTUnit.TestCaseContext;

import java.util.function.Consumer;

public interface CtxConsumer<T, R extends TestCaseContext<T>> extends Consumer<R> {}
