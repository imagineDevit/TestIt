package io.github.imagineDevit.giwt.expectations;

import io.github.imagineDevit.giwt.core.expectations.*;

import java.util.Arrays;

/**
 * [JExpectable] is an interface that extends [Expectable] and provides additional methods to verify expectations.
 * @param <T> the type of the result value
 */
public interface JExpectable<T> extends Expectable<T> {

    /**
     * @see Expectable#shouldFail(ExpectedToFail)
     */
    @Override
    default OnFailureChain<ExpectedToFail> shouldFail(ExpectedToFail expectation) {
        expectation.doVerify(resultError());
        return new OnFailureChain<>(resultError());
    }

    /**
     * Verifies that the result value should fail the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldFail(ExpectedToFail... expectations) {
        Arrays.asList(expectations).forEach(f -> f.doVerify(resultError()));
    }

    /**
     * @see Expectable#shouldBe(ExpectedToBe)
     */
    @Override
    default OnValueChain<T, ExpectedToBe<T>> shouldBe(ExpectedToBe<T> expectation) {
        return verify(expectation);
    }

    /**
     * Verifies that the result value should be the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldBe(ExpectedToBe... expectations) {
        verify(expectations);
    }

    /**
     * @see Expectable#shouldHave(ExpectedToHave)
     */
    @Override
    default OnValueChain<T, ExpectedToHave<T>> shouldHave(ExpectedToHave<T> expectation) {
        return verify(expectation);
    }

    /**
     * Verifies that the result value should have the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldHave(ExpectedToHave... expectations) {
        verify(expectations);
    }

    /**
     * @see Expectable#shouldMatch(ExpectedToMatch)
     */
    @Override
    default OnValueChain<T, ExpectedToMatch<T>> shouldMatch(ExpectedToMatch<T> expectation) {
        return verify(expectation);
    }

    /**
     * Verifies that the result value should match the provided expectations.
     *
     * @param expectations the expectations to be checked
     */
    default void shouldMatch(ExpectedToMatch... expectations) {
        verify(expectations);
    }

    /**
     * Verifies the provided expectations against the result value.
     *
     * @param expectations the expectations to be checked
     */
    private void verify(Expectation.OnValue<T>[] expectations) {
        T value = resultValue();
        Arrays.asList(expectations).forEach(b -> b.doVerify(value));
    }

    private <E extends Expectation.OnValue<T>> OnValueChain<T, E> verify(E expectation) {
        T value = resultValue();
        expectation.doVerify(value);
        return new OnValueChain<>(value, this);
    }
}
