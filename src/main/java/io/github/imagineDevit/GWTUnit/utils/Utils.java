package io.github.imagineDevit.GWTUnit.utils;


import io.github.imagineDevit.GWTUnit.TestConfiguration;
import io.github.imagineDevit.GWTUnit.TestParameters;
import io.github.imagineDevit.GWTUnit.annotations.*;
import io.github.imagineDevit.GWTUnit.callbacks.AfterAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.AfterEachCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeEachCallback;
import io.github.imagineDevit.GWTUnit.statements.StmtMsg;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.imagineDevit.GWTUnit.TestCase.DASH;

public abstract class Utils {

    public static String getTestName(String name, Method method) {
        if (name.isEmpty()) return method.getName();
        else return name;
    }

    public static void runCallbacks(Map<Object, List<Method>> methods, Function<Method, Integer> order) {
        methods
                .forEach((instance, ms) ->
                        ms.stream().sorted(Comparator.comparing(order))
                                .forEach(m -> ReflectionUtils.invokeMethod(m, instance))
                );
    }

    public static Map<Object, List<Method>> getBeforeAllMethods(Object testInstance) {
        return getCallbackMethods(testInstance, BeforeAll.class, BeforeAllCallback.class, "beforeAll");
    }

    public static Map<Object, List<Method>> getAfterAllMethods(Object testInstance) {
        return getCallbackMethods(testInstance, AfterAll.class, AfterAllCallback.class, "afterAll");
    }

    public static Map<Object, List<Method>> getBeforeEachMethods(Object testInstance) {
        return getCallbackMethods(testInstance, BeforeEach.class, BeforeEachCallback.class, "beforeEach");
    }

    public static Map<Object, List<Method>> getAfterEachMethods(Object testInstance) {
        return getCallbackMethods(testInstance, AfterEach.class, AfterEachCallback.class, "afterEach");
    }

    @SuppressWarnings("unchecked")
    public static List<? extends TestParameters.Parameter> getParametersFromMethod(Method method, String source) {
        return AnnotationSupport.findAnnotation(method, ParameterSource.class)
                .map(ParameterSource::value)
                .map(name -> {
                    if (Objects.equals(source, name)) {

                        Object instance = ReflectionUtils.newInstance(method.getDeclaringClass());

                        TestParameters<TestParameters.Parameter> testParameters = (TestParameters<TestParameters.Parameter>) ReflectionUtils.invokeMethod(method, instance);

                        return testParameters.getParameters();
                    }

                    return new ArrayList<TestParameters.Parameter>();

                }).orElse(new ArrayList<>());
    }

    public static List<? extends TestParameters.Parameter> getParametersFromConfiguration(TestConfiguration configuration, String source) {
        return configuration.getParameters(source)
                .map(TestParameters::getParameters)
                .orElse(new ArrayList<>());
    }

    public static List<? extends TestParameters.Parameter> getParameters(Method method, TestConfiguration configuration) {

        var testClass = method.getDeclaringClass();

        String parameterSource = method.getAnnotation(ParameterizedTest.class).source();

        List<Method> methodList = ReflectionUtils.findMethods(
                testClass,
                (Method m) -> AnnotationSupport.isAnnotated(m, ParameterSource.class) && m.getAnnotation(ParameterSource.class).value().equals(parameterSource)
        );

        return switch (methodList.size()) {
            case 0 -> {
                if(configuration != null) {
                    yield Utils.getParametersFromConfiguration(configuration, parameterSource);
                } else {
                    throw new IllegalStateException("No parameter source with name %s found".formatted(parameterSource));
                }
            }
            case 1 -> Utils.getParametersFromMethod(methodList.get(0), parameterSource);
            default -> throw new IllegalStateException("Multiple parameter sources with same name found (%s)".formatted(parameterSource));
        };

    }

    public static TestConfiguration getConfiguration(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        return Optional.ofNullable(clazz.getAnnotation(ConfigureWith.class))
                .map(ConfigureWith::value)
                .map(ReflectionUtils::newInstance)
                .orElse(null);
    }

    public static String reportTestCase(String name, List<StmtMsg> givenMsgs, List<StmtMsg> whenMsgs, List<StmtMsg> thenMsgs, TestParameters.Parameter parameters) {
        var n = name;

        if (parameters != null){
            n = parameters.formatName(n);
        }
        var title = TextUtils.bold("TEST CASE") + ": " + TextUtils.italic(TextUtils.purple(n));

        String givenMsg = givenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));
        String whenMsg = whenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));
        String thenMsg = thenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));

        StringBuilder sb = new StringBuilder();

        sb.append(
                """
                %s
                %s
                %s
                """.formatted(DASH, title, DASH)
        );
        if (!givenMsg.isEmpty()) {
            sb.append("""
                    %s
                    """.formatted(givenMsg));
        }

        if (!whenMsg.isEmpty()) {
            sb.append("""
                    %s
                    """.formatted(whenMsg));
        }

        if (!thenMsg.isEmpty()) {
            sb.append("""
                    %s
                    """.formatted(thenMsg));
        }

        sb.append("""
                %s
                """.formatted(DASH));

        return sb.toString();
    }

    public static <S> S runIfOpen(boolean closed, Supplier<S> fn, Runnable close) {
        if (closed) {
            throw new IllegalStateException("""
                                       
                    Test case is already closed.
                    A test case can only be run once.
                    """);
        }
        close.run();
        return fn.get();
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> Comparable<T> asComparableOrThrow(T value, Supplier<E> eSupplier) throws E {
        if (value instanceof Comparable<?> c) {
            return (Comparable<T>)  c;
        } else {
            throw eSupplier.get();
        }
    }


    private static Map<Object, List<Method>> getCallbackMethods(Object testInstance, Class<? extends Annotation> annotationClazz, Class<?> callbackClazz, String callbackMethod) {
        Map<Object, List<Method>> map = new HashMap<>();
        Class<?> testClass = testInstance.getClass();
        map.put(testInstance, (ReflectionUtils.findMethods(testClass, m -> AnnotationSupport.isAnnotated(m, annotationClazz))));

        Optional.ofNullable(testClass.getAnnotation(ExtendWith.class))
                .map(ExtendWith::value)
                .map(Arrays::asList)
                .orElse(new ArrayList<>())
                .forEach(clazz -> {
                    if (callbackClazz.isAssignableFrom(clazz)) {
                        ReflectionUtils.findMethod(clazz, callbackMethod)
                                .ifPresent(method ->
                                        map.put(ReflectionUtils.newInstance(clazz), Collections.singletonList(method))
                                );
                    }
                });

        return map;
    }


}
