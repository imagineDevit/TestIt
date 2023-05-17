package io.github.imagine.devit.TestIt;

import java.lang.reflect.Method;

public abstract class Utils {

    public static String getTestItName(String name, Method method){
        if (name.isEmpty()) return method.getName();
        else return name;
    }


}
