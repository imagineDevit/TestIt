package io.github.imagineDevit.giwt.processors;

import com.squareup.javapoet.*;
import io.github.imagineDevit.giwt.core.annotations.ParametersDataName;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A processor to generate proxy classes for classes annotated with {@link io.github.imagineDevit.giwt.core.annotations.TestProxy}.
 *
 * @author Henri Joel SEDJAME
 * @see io.github.imagineDevit.giwt.core.annotations.TestProxy
 */

@SupportedAnnotationTypes("io.github.imagineDevit.giwt.core.annotations.TestProxy")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class TestProxyProcessor extends AbstractProcessor {

    private static final String DELEGATE = "delegate";
    private static final String INIT = "<init>";
    private static final String P0 = "p0";
    private static final String TESTER = "TestProxy";
    private static final String PARAMS = "Params";

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation ->
                        roundEnv.getElementsAnnotatedWith(annotation).forEach(this::buildFile));
        return false;
    }

    private void buildFile(Element element) {
        var packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        var delegateName = element.getSimpleName().toString();
        var fileName = delegateName + TESTER;

        var type = TypeSpec.classBuilder(fileName)
                .addModifiers(Modifier.PUBLIC);

        var delegateField = FieldSpec
                .builder(TypeName.get(element.asType()), DELEGATE)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();

        var constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(element.asType()), DELEGATE)
                .addStatement("this.$N = $N", DELEGATE, DELEGATE)
                .build();

        type
                .addField(delegateField)
                .addMethod(constructor);

        var getters = element.getEnclosedElements()
                .stream()
                .filter(e -> e.getKind().isField())
                .map(e -> "get" + capitalize(e.getSimpleName().toString()))
                .toList();

        Predicate<ExecutableElement> predicate = e -> {
            var name = e.getSimpleName().toString();
            return !getters.contains(name)
                    && !name.equals(INIT)
                    && e.getModifiers().contains(Modifier.PUBLIC);
        };

        element.getEnclosedElements()
                .stream()
                .filter(e -> e instanceof ExecutableElement)
                .map(e -> (ExecutableElement) e)
                .filter(predicate)
                .forEach(e -> type.addMethod(buildMethod(type, e)));

        var file = JavaFile.builder(packageName, type.build()).build();

        try {
            file.writeTo(processingEnv.getFiler());
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }

    }

    private MethodSpec buildMethod(TypeSpec.Builder type, ExecutableElement element) {
        var methodName = element.getSimpleName().toString();

        TypeMirror elmtRetType = element.getReturnType();

        boolean isVoid = elmtRetType instanceof NoType;

        var returnType = isVoid ? TypeName.VOID : TypeName.get(elmtRetType);

        var method = MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC);

        var parameters = element.getParameters();

        var returnTerm = isVoid ? "" : "return";

        switch (parameters.size()) {
            case 0 -> method.addStatement("$N this.$N.$N()", returnTerm, DELEGATE, methodName);

            case 1 -> {
                String parameter = parameters.get(0).getSimpleName().toString();
                method.addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.get(parameters.get(0).asType()), parameter)
                        .addStatement("$N this.$N.$N($N)", returnTerm, DELEGATE, methodName, parameter);
            }
            default -> {
                var recordName = capitalize(Optional.ofNullable(element.getAnnotation(ParametersDataName.class))
                        .map(ParametersDataName::value)
                        .orElse(methodName) + PARAMS);

                type.addType(buildRecord(recordName, parameters));

                var paramValues = parameters
                        .stream()
                        .map(p -> "%s.%s".formatted(P0, p.getSimpleName().toString()))
                        .collect(Collectors.joining(", "));

                method.addModifiers(Modifier.PUBLIC)
                        .addParameter(ClassName.bestGuess(recordName), P0)
                        .addStatement("$N this.$N.$N($N)", returnTerm, DELEGATE, methodName, paramValues);
            }
        }

        return method
                .returns(returnType)
                .build();
    }

    private TypeSpec buildRecord(String recordName, List<? extends VariableElement> parameters) {

        var fields = parameters
                .stream()
                .map(p -> FieldSpec.builder(TypeName.get(p.asType()), p.getSimpleName().toString())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .build())
                .toList();

        var constructorParameters = parameters.stream()
                .map(p -> ParameterSpec.builder(TypeName.get(p.asType()), p.getSimpleName().toString()).build())
                .toList();

        var constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameters(constructorParameters);

        parameters.forEach(p -> constructor.addStatement("this.$N = $N", p.getSimpleName(), p.getSimpleName()));

        return TypeSpec.classBuilder(recordName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addFields(fields)
                .addMethod(constructor.build())
                .build();
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
