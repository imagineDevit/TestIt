package io.github.imagineDevit.GWTUnit.utils;

import io.github.imagineDevit.GWTUnit.annotations.Test;
import io.github.imagineDevit.GWTUnit.descriptors.TestItClassTestDescriptor;
import io.github.imagineDevit.GWTUnit.descriptors.TestItMethodTestDescriptor;
import io.github.imagineDevit.GWTUnit.descriptors.TestItParameterizedMethodTestDescriptor;
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
        var instance= ReflectionUtils.newInstance(clazz);
        if (TestItPredicates.isMethodTest().test(method)) {
            root.addChild(new TestItMethodTestDescriptor(
                    Utils.getTestName(method.getAnnotation(Test.class).value(), method),
                    method,
                    instance,
                    root.getUniqueId(),
                    null, null, null, null, null));

        } else if(TestItPredicates.isParameterizedMethodTest().test(method)) {
            root.addChild(new TestItParameterizedMethodTestDescriptor(method, null, instance, root.getUniqueId(), null, null, null, null));
        }
    }
}
