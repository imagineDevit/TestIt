package io.github.imagineDevit.giwt.statements.functions.context;

import io.github.imagineDevit.giwt.TestCaseContext;

import java.util.function.Function;

/**
 * TextCase context consumer.
 *
 * @param <R> type of the result value
 * @param <T> type of the state value
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 * @see TestCaseContext
 */
public interface GCtxFn<T, R> extends Function<TestCaseContext.GCtx<T, R>, T> {
}
