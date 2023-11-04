package io.github.imagineDevit.giwt.statements.functions;

import io.github.imagineDevit.giwt.TestCaseContext;

import java.util.function.Consumer;

public interface CtxConsumer<R, C extends TestCaseContext<?, R>> extends Consumer<C> {}
