package io.github.imagineDevit.GWTUnit;


import java.util.function.BiConsumer;

public interface ResCtxConsumer<R> extends BiConsumer<TestCaseContext<R>.TCtx, TestCaseResult<R>> {}
