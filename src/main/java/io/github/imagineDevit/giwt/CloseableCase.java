package io.github.imagineDevit.giwt;

import io.github.imagineDevit.giwt.utils.Utils;

import java.util.function.Supplier;

public abstract class CloseableCase {
    private boolean closed = false;

    private void close() {
        closed = true;
    }

    <S> S runIfOpen(Supplier<S> fn) {
        return Utils.runIfOpen(this.closed, fn, this::close);
    }
}
