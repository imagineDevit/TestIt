package io.github.imagineDevit.GWTUnit.assertions;

import io.github.imagineDevit.GWTUnit.TestCaseResult;

import java.util.Collection;

public record ShouldHave<T>(TestCaseResult.ResultValue.Ok<T> result) {

    public ShouldHave<T> size(int size) {

        int length;

        if (result.getValue() instanceof Collection<?> collection) {
            length = collection.size();
        } else if (result.getValue() instanceof String s) {
            length = s.length();
        } else {
            throw new AssertionError("Result value has no size");
        }

        if (length != size) {
            throw new AssertionError("Expected result to have size <" + size + "> but got <" + length + ">");
        }
        return this;
    }
    
    public <R> ShouldHave<T> anItemEqualTo(R element) {
        if (result.getValue() instanceof Collection<?> collection) {
            if (!collection.contains(element)){
                throw new AssertionError("Expected result to contain <" + element + "> but it does not");
            }
        } else {
            throw new AssertionError("Result value is not a collection");
        }

        return this;
    }

}
