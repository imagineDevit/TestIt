package io.github.imaginedevit.testIt.casefns;

import java.util.Optional;
import java.util.function.Consumer;

public interface ThenFn<R> extends Consumer<Optional<R>> {}
