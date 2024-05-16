package io.github.imagineDevit.giwt.utils;

import java.util.function.Supplier;

/**
 * A mutable value container class.
 *
 * @param <T> the type of the value to be stored
 * @author imagineDevit
 */
public class MutVal<T> {

    // The value stored in the container
    private Val value = new Val.Unsetted();

    /**
     * Returns the value if it is set, otherwise sets the value to the result of the supplied function and returns it.
     *
     * @param defaultValue a Supplier function that provides a default value if the value is not set
     * @return an Optional containing the value if it is set, otherwise an Optional containing the result of the supplied function
     */
    @SuppressWarnings("unchecked")
    public T getOr(Supplier<T> defaultValue) {
        if (value instanceof Val.Unsetted) {
            T t = defaultValue.get();
            value = new Val.Setted<>(t);
            return t;
        } else if (value instanceof Val.Setted<?> setted) {
            return (T) setted.value();
        }
        throw new IllegalStateException("Unknown value type");
    }

}
