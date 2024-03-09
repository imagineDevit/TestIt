package io.github.imagineDevit.giwt.statements.functions;

import io.github.imagineDevit.giwt.TestCaseContext;
import io.github.imagineDevit.giwt.TestCaseResult;

import java.util.function.BiConsumer;

/**
 * TextCase with context result  consumer.
 * @param <R> type of the result value
 * @see TestCaseContext
 * @version 0.1.2
 * @author Henri Joel SEDJAME
 */
public interface ResCtxConsumer<R> extends BiConsumer<TestCaseContext<?, R>.TCtx, TestCaseResult<R>> {
}
