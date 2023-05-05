package io.github.imaginedevit.testIt.casefns;

import java.util.function.Supplier;


@FunctionalInterface
public interface GivenFn<T> extends Supplier<T> {}
