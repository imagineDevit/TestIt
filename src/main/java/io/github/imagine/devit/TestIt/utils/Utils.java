package io.github.imagine.devit.TestIt.utils;

import io.github.imagine.devit.TestIt.annotations.*;
import io.github.imagine.devit.TestIt.callbacks.AfterAllCallback;
import io.github.imagine.devit.TestIt.callbacks.AfterEachCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeAllCallback;
import io.github.imagine.devit.TestIt.callbacks.BeforeEachCallback;
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
