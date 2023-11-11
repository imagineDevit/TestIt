package io.github.imagineDevit.giwt.statements.functions;


import io.github.imagineDevit.giwt.TestCaseContext;
import io.github.imagineDevit.giwt.TestCaseResult;

import java.util.function.BiConsumer;

public interface ResCtxConsumer<R> extends BiConsumer<TestCaseContext<?, R>.TCtx, TestCaseResult<R>> {}
