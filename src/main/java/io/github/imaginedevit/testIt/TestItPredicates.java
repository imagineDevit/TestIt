package io.github.imaginedevit.testIt;

import org.junit.platform.commons.support.AnnotationSupport;
import org.rapidpm.frp.model.Result;

import java.lang.reflect.Method;
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
                        .ifFailed(logger::info)
                        .ifPresent(b -> logger.info("selected " + clazz))
                        .getOrElse(() -> false);
    }


    public static Predicate<Method> isMethodTest() {
        return method -> match(
                matchCase(() -> Result.failure("This method is not supported by this TestEngine - " + method.getName())),
                matchCase(() -> isStatic(method), () -> Result.failure("No support for static methods " + method.getName())),
                matchCase(() -> isPrivate(method), () -> Result.failure("No support for private methods " + method.getName())),
                matchCase(() -> isAbstract(method), () -> Result.failure("No support for abstract methods " + method.getName())),
                matchCase(() ->
                                isTestClass().test(method.getDeclaringClass())
                                        && AnnotationSupport.isAnnotated(method, TestIt.class)
                                        && method.getParameterCount() == 1
                                        && method.getParameterTypes()[0].equals(TestCase.class)
                                        && method.getReturnType().equals(Void.TYPE)

                        , () -> Result.success(Boolean.TRUE))
        )
                .ifFailed(logger::info)
                .ifPresent(b -> logger.info("selected method " + method))
                .getOrElse(() -> false);
    }
}
