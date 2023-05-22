package io.github.imagineDevit.GWTUnit.utils;

import io.github.imagineDevit.GWTUnit.TestCase;
import io.github.imagineDevit.GWTUnit.TestItEngine;
import io.github.imagineDevit.GWTUnit.annotations.ParameterizedTest;
import io.github.imagineDevit.GWTUnit.annotations.Test;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;
import org.rapidpm.frp.model.Result;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static org.junit.platform.commons.util.ReflectionUtils.*;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;

public class TestItPredicates {
    private static final Logger logger = Logger.getLogger(TestItEngine.class.getName());

    public static Predicate<Class<?>> isTestClass() {

        return clazz ->
                match(
                        matchCase(() -> Result.failure("This class is not supported by this TestEngine - " + clazz.getSimpleName())),
                        matchCase(() -> isAbstract(clazz), () -> Result.failure("No support for abstract classes " + clazz.getSimpleName())),
                        matchCase(() -> isPrivate(clazz), () -> Result.failure("No support for private classes " + clazz.getSimpleName())),
                        matchCase(() -> !ReflectionUtils.findMethods(clazz, m -> isTestMethod(m) || isParameterizedTestMethod(m)).isEmpty(),
                                () -> Result.success(Boolean.TRUE))
                )
                        //.ifFailed(logger::severe)
                        .ifPresent(b -> logger.info("selected " + clazz))
                        .getOrElse(() -> false);
    }


    public static Predicate<Method> isMethodTest() {
        return method -> match(
                matchCase(() -> Result.failure("This method is not supported by this TestEngine - " + method.getName())),
                matchCase(() -> isStatic(method), () -> Result.failure("No support for static methods " + method.getName())),
                matchCase(() -> isPrivate(method), () -> Result.failure("No support for private methods " + method.getName())),
                matchCase(() -> isAbstract(method), () -> Result.failure("No support for abstract methods " + method.getName())),
                matchCase(() -> isTestMethod(method), () -> Result.success(Boolean.TRUE))
        )
                .ifPresent(b -> logger.info("selected method " + method))
                .getOrElse(() -> false);
    }

    public static Predicate<Method> isParameterizedMethodTest() {
        return method -> match(
                matchCase(() -> Result.failure("This method is not supported by this TestEngine - " + method.getName())),
                matchCase(() -> isStatic(method), () -> Result.failure("No support for static methods " + method.getName())),
                matchCase(() -> isPrivate(method), () -> Result.failure("No support for private methods " + method.getName())),
                matchCase(() -> isAbstract(method), () -> Result.failure("No support for abstract methods " + method.getName())),
                matchCase(() -> isParameterizedTestMethod(method), () -> Result.success(Boolean.TRUE))
        )
                .ifPresent(b -> logger.info("selected method " + method))
                .getOrElse(() -> false);
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
