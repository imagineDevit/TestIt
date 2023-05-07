package io.github.imaginedevit.testIt;

import java.util.function.Supplier;


@FunctionalInterface
public interface GivenFn<T> extends Supplier<T> {}
