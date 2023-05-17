package io.github.imagine.devit.TestIt;

import java.util.function.Supplier;


@FunctionalInterface
public interface GivenFn<T> extends Supplier<T> {}
