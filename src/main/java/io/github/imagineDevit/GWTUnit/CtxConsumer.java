package io.github.imagineDevit.GWTUnit;

import java.util.function.Consumer;

public interface CtxConsumer<T, R extends TestCaseContext<T>> extends Consumer<R> {}
