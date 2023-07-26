package io.github.imagineDevit.GWTUnit.annotations.processors;

import io.github.imagineDevit.GWTUnit.annotations.ParameterRecordName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.imagineDevit.GWTUnit.annotations.processors.Constants.*;

public record MethodProcessingData(
        String methodName,
        String methodReturnType,
        String paramRecordName,
        String paramName,
        String paramsWithType,
        String paramsValues
) {

    public static MethodProcessingData from(ExecutableElement method) {

        String name = method.getSimpleName().toString();

        List<? extends VariableElement> parameters = method.getParameters();

        var isRecord = false;
        String attributeName = switch (parameters.size()) {
            case 0 -> NULL;
            case 1 -> parameters.get(0).asType().toString();
            default -> {
                isRecord = true;
                yield Optional.ofNullable(method.getAnnotation(ParameterRecordName.class))
                        .map(ParameterRecordName::value)
                        .map(v -> {

                            if (v.isBlank() || v.endsWith(PARAMS)) return StringUtils.capitalize(v);

                            return StringUtils.capitalize(v) + PARAMS;
                        })
                        .orElse(StringUtils.capitalize(name) + PARAMS);
            }
        };


        String paramsWithName = parameters
                .stream()
                .map(p -> "%s %s".formatted(p.asType().toString(), p.getSimpleName().toString()))
                .collect(Collectors.joining(", "));

        String paramValues = isRecord ?
                parameters
                        .stream()
                        .map(p -> "%s.%s()".formatted(PARAM, p.getSimpleName().toString()))
                        .collect(Collectors.joining(", "))
                : PARAM;

        TypeMirror returnType = method.getReturnType();

        var methodReturnType = returnType.toString();

        if (returnType instanceof NoType) {
            methodReturnType = VOID;
        }

        return isRecord ? new MethodProcessingData(
                name,
                methodReturnType,
                attributeName,
                NULL,
                paramsWithName,
                paramValues
        ) : new MethodProcessingData(
                name,
                methodReturnType,
                NULL,
                attributeName,
                paramsWithName,
                paramValues
        );
    }
}
