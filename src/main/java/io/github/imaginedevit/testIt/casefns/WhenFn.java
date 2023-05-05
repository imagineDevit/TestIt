package io.github.imaginedevit.testIt.casefns;

import java.util.Optional;
import java.util.function.Supplier;

public interface WhenFn<R> extends Supplier<Optional<R>> {}
