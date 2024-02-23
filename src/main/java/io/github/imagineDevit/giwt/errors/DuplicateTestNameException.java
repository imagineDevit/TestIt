package io.github.imagineDevit.giwt.errors;

import io.github.imagineDevit.giwt.utils.TextUtils;

import java.util.List;
import java.util.stream.Collectors;

public final class DuplicateTestNameException extends GiwtError {

    public DuplicateTestNameException(List<String> names) {
        super(errorMessage(names));
    }

    private static String errorMessage(List<String> names){
        String testNames = names.stream()
                .map(TextUtils::green)
                .map("'%s'"::formatted)
                .collect(Collectors.joining(", "));
        return "Following test names : [ %s ] are duplicated.".formatted(testNames);
    }
}
