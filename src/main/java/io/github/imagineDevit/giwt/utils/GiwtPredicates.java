package io.github.imagineDevit.giwt.utils;

import io.github.imagineDevit.giwt.TestCase;
import io.github.imagineDevit.giwt.annotations.ParameterizedTest;
import io.github.imagineDevit.giwt.annotations.Test;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import static io.github.imagineDevit.giwt.utils.Matchers.MatchCase.matchCase;
import static io.github.imagineDevit.giwt.utils.Matchers.Result.failure;
import static io.github.imagineDevit.giwt.utils.Matchers.Result.success;
import static io.github.imagineDevit.giwt.utils.Matchers.match;
import static org.junit.platform.commons.util.ReflectionUtils.*;


public class GiwtPredicates {

    public static Predicate<Class<?>> isTestClass() {

        return clazz -> match(
                    matchCase(() -> isAbstract(clazz), () -> failure("No support for abstract classes " + clazz.getSimpleName())),
                    matchCase(() -> isPrivate(clazz), () -> failure("No support for private classes " + clazz.getSimpleName())),
                    matchCase(() -> !findMethods(clazz, m -> isTestMethod(m) || isParameterizedTestMethod(m)).isEmpty(),
                            () -> success(Boolean.TRUE))
            ).orElse(false);
    }

    public static Predicate<Method> isMethodTest() {
        return method -> match(
                matchCase(() -> isStatic(method), () -> failure("No support for static methods " + method.getName())),
                matchCase(() -> isPrivate(method), () -> failure("No support for private methods " + method.getName())),
                matchCase(() -> isAbstract(method), () -> failure("No support for abstract methods " + method.getName())),
                matchCase(() -> isTestMethod(method), () -> success(Boolean.TRUE))
        ).orElse(false);

    }

    public static Predicate<Method> isParameterizedMethodTest() {
        return method -> match(
                matchCase(() -> isStatic(method), () -> failure("No support for static methods " + method.getName())),
                matchCase(() -> isPrivate(method), () -> failure("No support for private methods " + method.getName())),
                matchCase(() -> isAbstract(method), () -> failure("No support for abstract methods " + method.getName())),
                matchCase(() -> isParameterizedTestMethod(method), () -> success(Boolean.TRUE))
        ).orElse(false);
    }

    private static boolean isTestMethod(Method method) {
        return AnnotationSupport.isAnnotated(method, Test.class)
                && method.getParameterCount() == 1
                && method.getParameterTypes()[0].equals(TestCase.class)
                && method.getReturnType().equals(Void.TYPE);
    }

    private static boolean isParameterizedTestMethod(Method method) {

        Class<?> testClass = method.getDeclaringClass();
        if (!AnnotationSupport.isAnnotated(method, ParameterizedTest.class))
            return false;


        String parameterSource = method.getAnnotation(ParameterizedTest.class).source();
        if (parameterSource.isEmpty() || method.getParameterCount() <= 1)
            return false;


        Optional<? extends Type> pClass = findMethod(testClass, parameterSource)
                .map(Method::getGenericReturnType);
        if (pClass.isEmpty()) return false;


        Type paramType = ((ParameterizedType) (pClass.get())).getActualTypeArguments()[0];
        Type[] pTypes = ((ParameterizedType) paramType).getActualTypeArguments();

        Class<?>[] parameterTypes = method.getParameterTypes();
        int length = parameterTypes.length;
        if (!parameterTypes[0].equals(TestCase.class)) return false;

        Class<?>[] paramsTypes = Arrays.copyOfRange(parameterTypes, 1, length);

        if (pTypes.length != paramsTypes.length) return false;

        for (int i = 0; i < paramsTypes.length; i++) {
            String typeName = paramsTypes[i].getTypeName();

            String expectedType = pTypes[i].getTypeName();
            if (!typeName.equals(expectedType)) return false;
        }

        return method.getReturnType().equals(Void.TYPE);
    }
}
