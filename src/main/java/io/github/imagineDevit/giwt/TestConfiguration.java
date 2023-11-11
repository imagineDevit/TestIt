package io.github.imagineDevit.giwt;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TestConfiguration {

    Map<String, TestParameters<?>> parameterSources();

    default Set<Class<?>> excludeFromReport() {
        return Set.of();
    }

    default Optional<TestParameters<?>> getParameters(String name){
        return Optional.ofNullable(parameterSources().get(name));
    }
}
