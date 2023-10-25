package io.github.imagineDevit.GWTUnit.utils;

import io.github.imagineDevit.GWTUnit.annotations.Test;
import io.github.imagineDevit.GWTUnit.callbacks.GwtCallbacks;
import io.github.imagineDevit.GWTUnit.descriptors.GwtClassTestDescriptor;
import io.github.imagineDevit.GWTUnit.descriptors.GwtMethodTestDescriptor;
import io.github.imagineDevit.GWTUnit.descriptors.GwtParameterizedMethodTestDescriptor;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.lang.reflect.Method;
import java.net.URI;

public class SelectorUtils {

    public static void appendTestInRoot(ClasspathRootSelector selector, EngineDescriptor root) {
        URI classpathRoot = selector.getClasspathRoot();
        ReflectionUtils
                .findAllClassesInClasspathRoot(classpathRoot, GwtPredicates.isTestClass(), (name) -> true)
                .forEach(testClass -> appendTestInClass(testClass, root));
    }

    public static void appendTestInPackage(String packageName, EngineDescriptor root) {
        ReflectionUtils.findAllClassesInPackage(packageName, GwtPredicates.isTestClass(), (name) -> true)
                .forEach(testClass -> appendTestInClass(testClass, root));
    }

    public static void appendTestInClass(Class<?> testClass, EngineDescriptor root) {
        if (GwtPredicates.isTestClass().test(testClass)) {
            root.addChild(new GwtClassTestDescriptor(testClass, root.getUniqueId()));
        }
    }

    public static void appendTestInMethod(Method method, EngineDescriptor root) {
        Class<?> clazz = method.getDeclaringClass();
        var instance= ReflectionUtils.newInstance(clazz);

        if (GwtPredicates.isMethodTest().test(method)) {
            root.addChild(new GwtMethodTestDescriptor(
                    Utils.getTestName(method.getAnnotation(Test.class).value(), method),
                    method,
                    instance,
                    root.getUniqueId(),
                    null, new GwtCallbacks(null, null, null, null)));

        } else if(GwtPredicates.isParameterizedMethodTest().test(method)) {
            root.addChild(new GwtParameterizedMethodTestDescriptor(method, null, instance, root.getUniqueId(), new GwtCallbacks(null, null, null, null), null));
        }
    }


}
