package io.github.imaginedevit.testIt;

import java.util.Optional;
import java.util.function.Function;


public interface GWhenFn<T,R> extends Function<Optional<T>,R> {}

