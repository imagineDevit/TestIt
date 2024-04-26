package io.github.imagineDevit.giwt.expectations;

import java.util.Collection;
import java.util.Map;

/**
 * This interface defines the expectations for a given value.
 * It is a sealed interface, meaning it can only be implemented by classes in the same module.
 * It provides two static methods to create specific expectations: size and anItemEqualTo.
 *
 * @param <T> the type of the value to be checked
 */
public sealed interface ExpectedToHave<T> extends Expectation.OnValue<T> {

    /**
     * Creates an expectation for a value to have a specific size.
     *
     * @param size the expected size
     * @param <T>  the type of the value to be checked
     * @return a Size expectation
     */
    static <T> Size<T> size(int size) {
        return new Size<>(size);
    }

    /**
     * Creates an expectation for a value to contain a specific item.
     *
     * @param item the expected item
     * @param <T>  the type of the value to be checked
     * @return an AnItemEqualTo expectation
     */
    static <T> AnItemEqualTo<T> anItemEqualTo(T item) {
        return new AnItemEqualTo<>(item);
    }

    /**
     * This record defines an expectation for a value to have a specific size.
     * It implements the verify method from the ExpectedToHave interface.
     *
     * @param <T> the type of the value to be checked
     */
    record Size<T>(int size) implements ExpectedToHave<T> {
        @Override
        public void verify(T value) {
            int length;

            if (value instanceof Collection<?> collection) length = collection.size();
            else if (value instanceof Object[] array) length = array.length;
            else if (value instanceof Map<?, ?> map) length = map.size();
            else if (value instanceof String s) length = s.length();
            else throw new AssertionError("Result value has no size");

            if (length != size)
                throw new AssertionError("Expected result to have size <" + size + "> but got <" + length + ">");
        }
    }

    /**
     * This record defines an expectation for a value to contain a specific item.
     * It implements the verify method from the ExpectedToHave interface.
     *
     * @param <T> the type of the value to be checked
     */
    record AnItemEqualTo<T>(T item) implements ExpectedToHave<T> {
        @Override
        public void verify(T value) {
            if (value instanceof Collection<?> collection) {
                if (!collection.contains(item))
                    throw new AssertionError("Expected result to contain <" + item + "> but it does not");
            } else if (value instanceof Object[] array) {
                boolean found = false;
                for (Object o : array) {
                    if (o.equals(item)) {
                        found = true;
                        break;
                    }
                }
                if (!found) throw new AssertionError("Expected result to contain <" + item + "> but it does not");
            } else {
                throw new AssertionError("Result value is not a collection");
            }
        }
    }
}