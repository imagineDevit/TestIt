package io.github.imagineDevit.giwt.statements.functions.context;

import io.github.imagineDevit.giwt.TestCaseContext;
import io.github.imagineDevit.giwt.TestCaseResult;

import java.util.function.BiConsumer;

/**
 * TextCase with context result  consumer.
 *
 * @param <R> type of the result value
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 * @see TestCaseContext
 */
public interface ResCtxConsumer<T, R> extends BiConsumer<TestCaseContext.TCtx<T, R>, TestCaseResult<R>> {
}
