package io.github.imagineDevit.GWTUnit;


import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class TestCaseResult<T> {

    private final T value;

    //region constructor
    private TestCaseResult(T value) {
        this.value = value;
    }

    public static <T> TestCaseResult<T> of(T value){
        if (value == null) throw new IllegalStateException("");
        return new TestCaseResult<>(value);
    }

    public static <T> TestCaseResult<T> empty() {
        return new TestCaseResult<>(null);
    }

    //endregion

    public <R> TestCaseResult<R> map(Function<T,R> mapper){
        if (value == null) return TestCaseResult.empty();
        return TestCaseResult.of(mapper.apply(value));
    }

    // region assertions

    public TestCaseResult<T> shouldBeNull() {
        assertNull(value);
        return this;
    }

    public TestCaseResult<T> shouldBeNotNull() {
        assertNotNull(value);
        return this;
    }

    public TestCaseResult<T> shouldBeNotEqualTo(T other){
        assertNotEquals(other, value);
        return this;
    }

    public TestCaseResult<T> shouldBeEqualTo(T other){
        assertEquals(other, value);
        return this;
    }

    public TestCaseResult<T> shouldMatch(Predicate<T> predicate){
        assertTrue(predicate.test(value));
        return this;
    }

    public TestCaseResult<T> shouldNotMatch(Predicate<T> predicate){
        assertFalse(predicate.test(value));
        return this;
    }


    @SafeVarargs
    public final void assertAll(Consumer<T>... consumers){
        for (Consumer<T> consumer : consumers) {
            consumer.accept(value);
        }
    }

    // endregion
}
