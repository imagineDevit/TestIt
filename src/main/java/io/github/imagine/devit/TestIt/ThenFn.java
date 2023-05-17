package io.github.imagine.devit.TestIt;

import java.util.Optional;
import java.util.function.Consumer;

public interface ThenFn<R> extends Consumer<Optional<R>> {}
