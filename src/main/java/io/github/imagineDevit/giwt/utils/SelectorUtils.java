package io.github.imagineDevit.giwt.utils;

import io.github.imagineDevit.giwt.annotations.Test;
import io.github.imagineDevit.giwt.callbacks.GiwtCallbacks;
import io.github.imagineDevit.giwt.descriptors.GiwtClassTestDescriptor;
import io.github.imagineDevit.giwt.descriptors.GiwtMethodTestDescriptor;
import io.github.imagineDevit.giwt.descriptors.GiwtParameterizedMethodTestDescriptor;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.lang.reflect.Method;
import java.net.URI;

public class SelectorUtils {

    public static void appendTestInRoot(ClasspathRootSelector selector, EngineDescriptor root) {
        URI classpathRoot = selector.getClasspathRoot();
        ReflectionUtils
                .findAllClassesInClasspathRoot(classpathRoot, GiwtPredicates.isTestClass(), (name) -> true)
                .forEach(testClass -> appendTestInClass(testClass, root));
    }

    public static void appendTestInPackage(String packageName, EngineDescriptor root) {
        ReflectionUtils.findAllClassesInPackage(packageName, GiwtPredicates.isTestClass(), (name) -> true)
                .forEach(testClass -> appendTestInClass(testClass, root));
    }

    public static void appendTestInClass(Class<?> testClass, EngineDescriptor root) {
        if (GiwtPredicates.isTestClass().test(testClass)) {
            root.addChild(new GiwtClassTestDescriptor(testClass, root.getUniqueId()));
        }
    }

    public static void appendTestInMethod(Method method, EngineDescriptor root) {
        Class<?> clazz = method.getDeclaringClass();
        var instance= ReflectionUtils.newInstance(clazz);

        if (GiwtPredicates.isMethodTest().test(method)) {
            root.addChild(new GiwtMethodTestDescriptor(
                    Utils.getTestName(method.getAnnotation(Test.class).value(), method),
                    method,
                    instance,
                    root.getUniqueId(),
                    null, new GiwtCallbacks(null, null, null, null)));

        } else if(GiwtPredicates.isParameterizedMethodTest().test(method)) {
            root.addChild(new GiwtParameterizedMethodTestDescriptor(method, null, instance, root.getUniqueId(), new GiwtCallbacks(null, null, null, null), null));
        }
    }


}
