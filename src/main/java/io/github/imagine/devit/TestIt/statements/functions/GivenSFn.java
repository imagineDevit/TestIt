package io.github.imagine.devit.TestIt.statements.functions;

import java.util.function.Supplier;


@FunctionalInterface
public interface GivenSFn<T> extends Supplier<T> {}
