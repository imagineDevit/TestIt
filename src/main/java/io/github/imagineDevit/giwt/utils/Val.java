package io.github.imagineDevit.giwt.utils;

import java.util.Optional;

/**
 * The Val interface represents a value that can be either set or unset.
 *
 * @author imagineDevit
 */
public sealed interface Val {

    /**
     * The Unsetted record represents an unset value.
     */
    record Unsetted() implements Val {
    }

    /**
     * The Setted record represents a set value.
     *
     * @param value the set value
     * @param <T>   the type of the value
     */
    record Setted<T>(T value) implements Val {
    }
}