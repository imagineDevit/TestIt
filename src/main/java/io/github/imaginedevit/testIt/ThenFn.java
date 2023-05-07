package io.github.imaginedevit.testIt;

import java.util.Optional;
import java.util.function.Consumer;

public interface ThenFn<R> extends Consumer<Optional<R>> {}
