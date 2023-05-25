package io.github.imagineDevit.GWTUnit.utils;


import io.github.imagineDevit.GWTUnit.TestConfiguration;
import io.github.imagineDevit.GWTUnit.TestParameters;
import io.github.imagineDevit.GWTUnit.callbacks.AfterAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.AfterEachCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeAllCallback;
import io.github.imagineDevit.GWTUnit.callbacks.BeforeEachCallback;
import io.github.imagineDevit.GWTUnit.annotations.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

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

    public static List<? extends TestParameters.Parameter> getParameters(Method method){

        var testClass = method.getDeclaringClass();

        String parameterSource = method.getAnnotation(ParameterizedTest.class).source();

        List<Method> methodList = ReflectionUtils.findMethods(
                testClass,
                (Method m) -> AnnotationSupport.isAnnotated(m, ParameterSource.class) && m.getAnnotation(ParameterSource.class).value().equals(parameterSource)
        );

        return switch (methodList.size()) {
            case 0 -> {
                if(AnnotationSupport.isAnnotated(testClass, ConfigureWith.class)) {
                    Class<? extends TestConfiguration> configClass = testClass.getAnnotation(ConfigureWith.class).value();
                    yield Utils.getParametersFromConfiguration(ReflectionUtils.newInstance(configClass), parameterSource);
                } else {
                    throw new IllegalStateException("No parameter source with name %s found".formatted(parameterSource));
                }
            }
            case 1 -> Utils.getParametersFromMethod(methodList.get(0), parameterSource);
            default -> throw new IllegalStateException("Multiple parameter sources with same name found (%s)".formatted(parameterSource));
        };

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
