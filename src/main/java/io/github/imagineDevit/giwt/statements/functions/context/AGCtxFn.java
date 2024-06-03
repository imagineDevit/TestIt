package io.github.imagineDevit.giwt.statements.functions.context;

import io.github.imagineDevit.giwt.TestCaseContext;

import java.util.function.BiConsumer;

/**
 * TextCase context consumer.
 *
 * @param <R> type of the result value
 * @param <T> type of the state value
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 * @see TestCaseContext
 */
public interface AGCtxFn<T, R> extends BiConsumer<TestCaseContext.AGCtx<T, R>, T> {
}
