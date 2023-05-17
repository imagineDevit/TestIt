package io.github.imagine.devit.TestIt;

import org.junit.platform.commons.support.AnnotationSupport;
import org.rapidpm.frp.model.Result;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static org.junit.platform.commons.util.ReflectionUtils.*;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;

public class TestItPredicates {
    private static final Logger logger = Logger.getLogger(TestItEngine.class.getName());

    protected static Predicate<Class<?>> isTestClass() {

        return clazz ->
                match(
                        matchCase(() -> Result.failure("This class is not supported by this TestEngine - " + clazz.getSimpleName())),
                        matchCase(() -> isAbstract(clazz), () -> Result.failure("No support for abstract classes " + clazz.getSimpleName())),
                        matchCase(() -> isPrivate(clazz), () -> Result.failure("No support for private classes " + clazz.getSimpleName())),
                        matchCase(() -> AnnotationSupport.isAnnotated(clazz, TestItClass.class), () -> Result.success(Boolean.TRUE))
                )
                        .ifFailed(logger::severe)
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
                .ifFailed(logger::severe)
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
                .ifFailed(logger::severe)
                .ifPresent(b -> logger.info("selected method " + method))
                .getOrElse(() -> false);
    }

    private static boolean isTestMethod(Method method) {
        return isTestClass().test(method.getDeclaringClass())
                && logIfFalse("Test Method must be annotated with @TestIt", () -> AnnotationSupport.isAnnotated(method, TestIt.class))
                && method.getParameterCount() == 1
                && method.getParameterTypes()[0].equals(TestCase.class)
                && method.getReturnType().equals(Void.TYPE);
    }

    private static boolean isParameterizedTestMethod(Method method) {

        Class<?> testClass = method.getDeclaringClass();
        if (!isTestClass().test(testClass)) return false;
        if (!AnnotationSupport.isAnnotated(method, ParameterizedTestIt.class))
            return logAndReturnFalse("Test Method must be annotated with @ParameterizedTestIt");


        String parameterSource = method.getAnnotation(ParameterizedTestIt.class).source();
        if (parameterSource.isEmpty() || method.getParameterCount() <= 1)
            return logAndReturnFalse("Parameterized test method must have more than one parameters");


        Optional<? extends Type> pClass = findMethod(testClass, parameterSource)
                .map(Method::getGenericReturnType);
        if (pClass.isEmpty()) return logAndReturnFalse("ParameterMethod source must return a value of type TestParameters<T extends TestParameters.Parameter>");


        Type paramType = ((ParameterizedType) (pClass.get())).getActualTypeArguments()[0];
        Type[] pTypes = ((ParameterizedType) paramType).getActualTypeArguments();

        Class<?>[] parameterTypes = method.getParameterTypes();
        int length = parameterTypes.length;
        if (!parameterTypes[0].equals(TestCase.class)) return logAndReturnFalse("The first parameter of the method test must be of type TestCase");

        Class<?>[] paramsTypes = Arrays.copyOfRange(parameterTypes, 1, length);

        if (pTypes.length != paramsTypes.length) return logAndReturnFalse("Expect %d test parameters but found %d".formatted(pTypes.length, paramsTypes.length));

        for (int i = 0; i < paramsTypes.length; i++) {
            String typeName = paramsTypes[i].getTypeName();

            String expectedType = pTypes[i].getTypeName();
            if (!typeName.equals(expectedType)) return logAndReturnFalse("Expect %s type for parameter in position %d but found type %s".formatted(
                    expectedType, i, typeName
            ));
        }

        return method.getReturnType().equals(Void.TYPE);
    }


    private static boolean logAndReturnFalse(String message){
        logger.severe(message);
        return false;
    }

    private static boolean logIfFalse(String message, BooleanSupplier bloc){
        boolean r = bloc.getAsBoolean();

        if (!r) logger.severe(message);

        return r;
    }
}
