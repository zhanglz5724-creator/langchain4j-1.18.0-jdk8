/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.classinstance;

import dev.langchain4j.spi.classloading.ClassInstanceFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.ServiceLoader;

public final class ClassInstanceLoader {
    private ClassInstanceLoader() {
    }

    public static <T> T getClassInstance(Class<T> clazz) {
        Iterator<ClassInstanceFactory> iterator = ServiceLoader.load(ClassInstanceFactory.class).iterator();
        if (iterator.hasNext()) {
            ClassInstanceFactory factory = iterator.next();
            return factory.getInstanceOfClass(clazz);
        }
        return ClassInstanceLoader.createNewClassInstance(clazz);
    }

    private static <T> T createNewClassInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

