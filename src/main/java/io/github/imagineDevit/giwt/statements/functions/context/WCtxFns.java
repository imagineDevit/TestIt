package io.github.imagineDevit.giwt.statements.functions.context;

import io.github.imagineDevit.giwt.TestCaseContext;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * TextCase with context state consumer.
 *
 * @param <T> type of the state value
 * @param <R> type of the result value
 * @author Henri Joel SEDJAME
 * @version 0.1.2
 * @see TestCaseContext
 */

sealed public interface WCtxFns<T, R> {
    non-sealed interface WCtxSFn<T, R> extends WCtxFns<T, R>, BiFunction<TestCaseContext.WCtx<T, R>, T, R> {
    }

    non-sealed interface WCtxFFn<T, R> extends WCtxFns<T, R>, Consumer<TestCaseContext.WCtx<T, R>> {
    }
}

