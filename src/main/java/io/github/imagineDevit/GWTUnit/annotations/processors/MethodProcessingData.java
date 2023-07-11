package io.github.imagineDevit.GWTUnit.annotations.processors;

import io.github.imagineDevit.GWTUnit.annotations.ParameterRecordName;
import org.apache.commons.lang.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.imagineDevit.GWTUnit.annotations.processors.Constants.*;

public record MethodProcessingData(

        String methodName,

        String methodReturnType,

        String paramRecordName,
        String paramsWithType,
        String paramsValues
) {

    public static MethodProcessingData from(ExecutableElement method) {

        String name = method.getSimpleName().toString();

        String paramRecordName = Optional.ofNullable(method.getAnnotation(ParameterRecordName.class))
                .map(ParameterRecordName::value)
                .map(v -> {
                    if (v.isBlank()) return null;

                    if (v.endsWith(PARAMS)) return StringUtils.capitalize(v);

                    return StringUtils.capitalize(v) + PARAMS;
                })
                .orElse(StringUtils.capitalize(name) + PARAMS);


        String paramsWithName = method.getParameters()
                .stream()
                .map(p -> "%s %s".formatted( p.asType().toString(), p.getSimpleName().toString()))
                .collect(Collectors.joining(", "));

        String paramValues = method.getParameters()
                .stream()
                .map(p -> "%s.%s()".formatted(PARAM, p.getSimpleName().toString()))
                .collect(Collectors.joining(", "));


        TypeMirror returnType = method.getReturnType();

        var methodReturnType = returnType.toString();

        if (returnType instanceof NoType) {
            methodReturnType = VOID;
        }

        return new MethodProcessingData(
                name,
                methodReturnType,
                paramRecordName,
                paramsWithName,
                paramValues
        );
    }
}
