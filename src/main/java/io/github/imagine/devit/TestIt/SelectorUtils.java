package io.github.imagine.devit.TestIt;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.lang.reflect.Method;
import java.net.URI;

public class SelectorUtils {

    public static void appendTestInRoot(ClasspathRootSelector selector, EngineDescriptor root) {
        URI classpathRoot = selector.getClasspathRoot();
        ReflectionUtils
                .findAllClassesInClasspathRoot(classpathRoot, TestItPredicates.isTestClass(), (name) -> true)
                .forEach(testClass -> appendTestInClass(testClass, root));
    }

    public static void appendTestInPackage(String packageName, EngineDescriptor root) {
        ReflectionUtils.findAllClassesInPackage(packageName, TestItPredicates.isTestClass(), (name) -> true)
                .forEach(testClass -> appendTestInClass(testClass, root));
    }

    public static void appendTestInClass(Class<?> testClass, EngineDescriptor root) {
        if (TestItPredicates.isTestClass().test(testClass)) {
            root.addChild(new TestItClassTestDescriptor(testClass, root.getUniqueId()));
        }
    }

    public static void appendTestInMethod(Method method, EngineDescriptor root) {
        Class<?> clazz = method.getDeclaringClass();
        if (TestItPredicates.isMethodTest().test(method)) {
            root.addChild(new TestItMethodTestDescriptor(
                    Utils.getTestItName(method.getAnnotation(TestIt.class).value(), method),
                    method,
                    clazz,
                    root.getUniqueId(),
                    null));

        } else if(TestItPredicates.isParameterizedMethodTest().test(method)) {
            String parameterSource = method.getAnnotation(ParameterizedTestIt.class).source();
            Method sourceMethod = ReflectionUtils.findMethod(clazz, parameterSource).orElseThrow();
            root.addChild(new TestItParameterizedMethodTestDescriptor(method, sourceMethod, clazz, root.getUniqueId()));
        }
    }
}
