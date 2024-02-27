package io.github.imagineDevit.giwt.utils;


import io.github.imagineDevit.giwt.TestConfiguration;
import io.github.imagineDevit.giwt.TestParameters;
import io.github.imagineDevit.giwt.annotations.*;
import io.github.imagineDevit.giwt.callbacks.AfterAllCallback;
import io.github.imagineDevit.giwt.callbacks.AfterEachCallback;
import io.github.imagineDevit.giwt.callbacks.BeforeAllCallback;
import io.github.imagineDevit.giwt.callbacks.BeforeEachCallback;
import io.github.imagineDevit.giwt.callbacks.Callback;
import io.github.imagineDevit.giwt.errors.DuplicateTestNameException;
import io.github.imagineDevit.giwt.errors.TestCaseArgMissingException;
import io.github.imagineDevit.giwt.statements.StmtMsg;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.imagineDevit.giwt.TestCase.DASH;

public abstract class Utils {

    public static String getTestName(String name, Method method) {
        if (name.isEmpty()) return method.getName();
        else return name;
    }

    public static String getTestName(Method method) {
        return Optional.ofNullable(method.getAnnotation(Test.class))
                .map(Test::value)
                .or(() -> Optional.ofNullable(method.getAnnotation(ParameterizedTest.class).name()))
                .or(() -> Optional.of(""))
                .map(s -> getTestName(s, method))
                .get();
    }

    public static void runCallbacks(Map<Object, List<Method>> methods, Function<Method, Integer> order) {
        methods
                .forEach((instance, ms) ->
                        ms.stream().sorted(Comparator.comparing(order))
                                .forEach(m -> ReflectionUtils.invokeMethod(m, instance))
                );
    }

    public static Map<Object, List<Method>> getBeforeAllMethods(Object testInstance) {
        return getCallbackMethods(testInstance, BeforeAll.class, BeforeAllCallback.class, Callback.Methods.BEFORE_ALL);
    }

    public static Map<Object, List<Method>> getAfterAllMethods(Object testInstance) {
        return getCallbackMethods(testInstance, AfterAll.class, AfterAllCallback.class, Callback.Methods.AFTER_ALL);
    }

    public static Map<Object, List<Method>> getBeforeEachMethods(Object testInstance) {
        return getCallbackMethods(testInstance, BeforeEach.class, BeforeEachCallback.class, Callback.Methods.BEFORE_EACH);
    }

    public static Map<Object, List<Method>> getAfterEachMethods(Object testInstance) {
        return getCallbackMethods(testInstance, AfterEach.class, AfterEachCallback.class, Callback.Methods.AFTER_EACH);
    }

    @SuppressWarnings("unchecked")
    public static List<? extends TestParameters.Parameter> getParametersFromMethod(Method method, String source) {
        return AnnotationSupport.findAnnotation(method, ParameterSource.class)
                .map(ParameterSource::value)
                .map(name -> {
                    if (Objects.equals(source, name)) {

                        var instance = ReflectionUtils.newInstance(method.getDeclaringClass());

                        var testParameters = (TestParameters<TestParameters.Parameter>) ReflectionUtils.invokeMethod(method, instance);

                        return testParameters.getParameters();
                    }

                    return new ArrayList<TestParameters.Parameter>();

                }).orElse(Collections.emptyList());
    }

    public static List<? extends TestParameters.Parameter> getParametersFromConfiguration(TestConfiguration configuration, String source) {
        return configuration.getParameters(source)
                .map(TestParameters::getParameters)
                .orElse(new ArrayList<>());
    }

    public static List<? extends TestParameters.Parameter> getParameters(Method method, TestConfiguration configuration) {

        var testClass = method.getDeclaringClass();

        var parameterSource = method.getAnnotation(ParameterizedTest.class).source();

        var methodList = ReflectionUtils.findMethods(
                testClass,
                (Method m) -> AnnotationSupport.isAnnotated(m, ParameterSource.class) && m.getAnnotation(ParameterSource.class).value().equals(parameterSource)
        );

        return switch (methodList.size()) {
            case 0 -> {
                if (configuration != null) {
                    yield Utils.getParametersFromConfiguration(configuration, parameterSource);
                } else {
                    throw new IllegalStateException("No parameter source with name %s found".formatted(parameterSource));
                }
            }
            case 1 -> Utils.getParametersFromMethod(methodList.get(0), parameterSource);
            default ->
                    throw new IllegalStateException("Multiple parameter sources with same name found (%s)".formatted(parameterSource));
        };

    }

    public static TestConfiguration getConfiguration(Method method) {
        var clazz = method.getDeclaringClass();
        return Optional.ofNullable(clazz.getAnnotation(ConfigureWith.class))
                .map(ConfigureWith::value)
                .map(ReflectionUtils::newInstance)
                .orElse(null);
    }

    public static String reportTestCase(String name, List<StmtMsg> givenMsgs, List<StmtMsg> whenMsgs, List<StmtMsg> thenMsgs, TestParameters.Parameter parameters) {
        var n = name;

        if (parameters != null) n = parameters.formatName(n);

        var title = TextUtils.bold("▶️" + TextUtils.italic(TextUtils.purple(n)));

        var givenMsg = givenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));
        var whenMsg = whenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));
        var thenMsg = thenMsgs.stream().map(StmtMsg::value).collect(Collectors.joining("\n"));

        var sb = new StringBuilder();

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
                                       \s
                    Test case is already closed.
                    A test case can only be run once.
                   \s""");
        }
        close.run();
        return fn.get();
    }

    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> Comparable<T> asComparableOrThrow(T value, Supplier<E> eSupplier) throws E {
        if (value instanceof Comparable<?> c) {
            return (Comparable<T>) c;
        } else {
            throw eSupplier.get();
        }
    }

    public static void checkTestNamesDuplication(Class<?> testClass) {
        var methods = ReflectionUtils.findMethods(testClass, GiwtPredicates.isMethodTest().or(GiwtPredicates.isParameterizedMethodTest()));

        var duplicatedTestNames = findDuplicatedTestNames(methods);

        if (!duplicatedTestNames.isEmpty()) {
            throw new DuplicateTestNameException(duplicatedTestNames);
        }
    }

    public static void checkTestCaseArgPresent(Class<?> testClass) {
        List<Method> methods = ReflectionUtils.findMethods(testClass, GiwtPredicates.isTestMethodWithoutTestCaseArg());

        if (!methods.isEmpty()) {
            throw new TestCaseArgMissingException(methods.stream().map(Method::getName).toList());
        }
    }

    public static void checkTestCaseArgPresent(Method testMethod) {
        if (GiwtPredicates.isTestMethodWithoutTestCaseArg().test(testMethod)) {
            throw new TestCaseArgMissingException(testMethod.getName());
        }
    }

    private static List<String> findDuplicatedTestNames(List<Method> methods) {
        var testNames = methods.stream()
                .map(m ->
                        Optional.ofNullable(m.getAnnotation(Test.class))
                                .map(Test::value)
                                .orElseGet(() -> m.getAnnotation(ParameterizedTest.class).name())
                ).toList();

        return testNames.stream()
                .distinct()
                .filter(s -> !s.isEmpty())
                .filter(s -> testNames.stream().filter(tn -> tn.equals(s)).count() > 1)
                .toList();
    }

    private static Map<Object, List<Method>> getCallbackMethods(Object testInstance, Class<? extends Annotation> annotationClazz, Class<? extends Callback> callbackClazz, String callbackMethod) {
        var map = new HashMap<Object, List<Method>>();
        var testClass = testInstance.getClass();

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
