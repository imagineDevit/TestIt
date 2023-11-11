package io.github.imagineDevit.giwt;

import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

public class TestParameters<T extends TestParameters.Parameter> {

    private final List<T> parameters = new ArrayList<>();

    @SafeVarargs
    public static <R extends Parameter> TestParameters<R> of(R... parameters) {
        TestParameters<R> testParameters = new TestParameters<>();
        Collections.addAll(testParameters.parameters, parameters);
        return testParameters;
    }

    public List<T> getParameters() {
        return parameters;
    }

    public abstract static class Parameter {

        Object[] paramValues;

        public Parameter(int len, Object... args) {
            paramValues = new Object[len];
            System.arraycopy(args, 0, paramValues, 0, len);
        }

        abstract void executeTest(Object testInstance, Method method, TestCase<?,?> testCase);

        public String formatName(String name) {
            for (int i = 0; i < paramValues.length; i++) {
                name = name.replace("{%s}".formatted(i), paramValues[i].toString());
            }
            return name;
        }

        public final static class P1<T> extends Parameter {
            private P1(T args) {
                super(1, args);
            }

            public static <T> P1<T> of(T args) {
                return new P1<>(args);
            }

            @Override
            void executeTest(Object testInstance, Method method, TestCase<?, ?> testCase) {
                ReflectionUtils.invokeMethod(method, testInstance, testCase, paramValues[0]);
            }
        }

        public final static class P2<T, R> extends Parameter {
            private P2(T arg1, R arg2) {
                super(2, arg1, arg2);
            }

            public static <T, R> P2<T, R> of(T arg1, R arg2) {
                return new P2<>(arg1, arg2);
            }

            @Override
            void executeTest(Object testInstance, Method method, TestCase<?, ?> testCase) {
                ReflectionUtils.invokeMethod(method, testInstance, testCase, paramValues[0], paramValues[1]);
            }
        }

        public final static class P3<T, R, S> extends Parameter {
            private P3(T arg1, R arg2, S arg3) {
                super(3, arg1, arg2, arg3);
            }

            public static <T, R, S> P3<T, R, S> of(T arg1, R arg2, S arg3) {
                return new P3<>(arg1, arg2, arg3);
            }

            @Override
            void executeTest(Object testInstance, Method method, TestCase<?, ?> testCase) {
                ReflectionUtils.invokeMethod(method, testInstance, testCase, paramValues[0], paramValues[1], paramValues[2]);
            }
        }

        public final static class P4<T, R, S, U> extends Parameter {
            private P4(T arg1, R arg2, S arg3, U arg4) {
                super(4, arg1, arg2, arg3, arg4);
            }

            public static <T, R, S, U> P4<T, R, S, U> of(T arg1, R arg2, S arg3, U arg4) {
                return new P4<>(arg1, arg2, arg3, arg4);
            }

            @Override
            void executeTest(Object testInstance, Method method, TestCase<?, ?> testCase) {
                ReflectionUtils.invokeMethod(method, testInstance, testCase, paramValues[0], paramValues[1], paramValues[2], paramValues[3]);
            }

        }

        public final static class P5<T, R, S, U, V> extends Parameter {
            private P5(T arg1, R arg2, S arg3, U arg4, V arg5) {
                super(5, arg1, arg2, arg3, arg4, arg5);
            }

            public static <T, R, S, U, V> P5<T, R, S, U, V> of(T arg1, R arg2, S arg3, U arg4, V arg5) {
                return new P5<>(arg1, arg2, arg3, arg4, arg5);
            }

            @Override
            void executeTest(Object testInstance, Method method, TestCase<?, ?> testCase) {
                ReflectionUtils.invokeMethod(method, testInstance, testCase, paramValues[0], paramValues[1], paramValues[2], paramValues[3], paramValues[4]);
            }

        }
    }

}


