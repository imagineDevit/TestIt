package io.github.imagineDevit.GWTUnit.statements.functions;


import io.github.imagineDevit.GWTUnit.TestCaseContext;
import io.github.imagineDevit.GWTUnit.TestCaseResult;

import java.util.function.BiConsumer;

public interface ResCtxConsumer<R> extends BiConsumer<TestCaseContext<R>.TCtx, TestCaseResult<R>> {}
