package io.github.imagineDevit.giwt.assertions;

import java.util.Collection;
import java.util.Map;

public sealed interface ShouldHaves<T> extends Expectation.Succeed<T> {

    static <T> Size<T> size(int size) {
        return new Size<>(size);
    }

    static <T> AnItemEqualTo<T> anItemEqualTo(T item) {
        return new AnItemEqualTo<>(item);
    }

    record Size<T>(int size) implements ShouldHaves<T> {
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

    record AnItemEqualTo<T>(T item) implements ShouldHaves<T> {
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
