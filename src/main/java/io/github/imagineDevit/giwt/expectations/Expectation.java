package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.utils.TextUtils;

import java.util.function.Consumer;

/**
 * This interface represents an expectation, which is a condition that a value must satisfy.
 * An expectation can either succeed or fail.
 * It provides a method to verify the expectation against a value.
 *
 * @param <T> the type of the value to be checked
 * @author Henri Joel SEDJAME
 * @version 0.1.5
 */
public sealed interface Expectation<T> {

    String PLAY = "▸";
    String PASSED = " ✔";
    String FAILED = " ✘";

    sealed interface Name {

        record Value(String name) implements Name {
            public Value {
                if (name == null || name.isBlank()) {
                    throw new IllegalArgumentException("Name cannot be null or blank");
                }
            }
        }

        final class None implements Name {}


    }

    /**
     *  get the name of the expectation.
     * @return a [Name] instance
     */
    Name name();

    /**
     * Verifies the expectation against a value.
     *
     * @param value the value to be checked
     */
    void verify(T value);

    default void doVerify(T value) {
        Name name = name();
        Consumer<Consumer<String>> doPrint = (Consumer<String> c) -> {
            if (name instanceof Name.Value v) {
                c.accept(v.name());
            }
        };
        try {
            doPrint.accept(s -> System.out.printf("     %s %s", TextUtils.blue(PLAY), TextUtils.italic(s)));
            verify(value);
            doPrint.accept(s -> System.out.println( TextUtils.green(PASSED)));
        } catch (Throwable e) {
            doPrint.accept(s -> System.out.println(TextUtils.red(FAILED)));
            throw e;
        }
    }

    /**
     * This interface represents a failed expectation.
     * It extends the Expectation interface with the type parameter set to Exception.
     * It can only be implemented by the ExpectedToFail class.
     */
    sealed interface OnFailure extends Expectation<Throwable> permits ExpectedToFail {
    }

    /**
     * This interface represents a successful expectation.
     * It extends the Expectation interface with a generic type parameter.
     * It can be implemented by the ExpectedToBe, ExpectedToHave, and ExpectedToMatch classes.
     *
     * @param <T> the type of the value to be checked
     */
    sealed interface OnValue<T> extends Expectation<T> permits ExpectedToBe, ExpectedToHave, ExpectedToMatch {
    }
}