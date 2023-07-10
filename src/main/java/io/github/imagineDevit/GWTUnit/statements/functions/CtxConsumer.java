package io.github.imagineDevit.GWTUnit.statements.functions;

import io.github.imagineDevit.GWTUnit.TestCaseContext;

import java.util.function.Consumer;

public interface CtxConsumer<R, C extends TestCaseContext<?, R>> extends Consumer<C> {}
