package io.github.imagineDevit.giwt.statements.functions.whens;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface WhenFn {

    non-sealed interface C<T> extends Consumer<T>, WhenFn {
    }

    non-sealed interface S<R> extends Supplier<R>, WhenFn {
    }

    non-sealed interface F<T, R> extends Function<T, R>, WhenFn {
    }

    non-sealed interface R extends Runnable, WhenFn {
    }
}
