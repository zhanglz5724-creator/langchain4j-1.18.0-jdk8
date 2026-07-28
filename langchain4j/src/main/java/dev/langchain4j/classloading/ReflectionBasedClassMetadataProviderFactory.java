/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.spi.classloading.ClassMetadataProviderFactory
 */
package dev.langchain4j.classloading;

import dev.langchain4j.spi.classloading.ClassMetadataProviderFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionBasedClassMetadataProviderFactory
implements ClassMetadataProviderFactory<Method> {
    public <T extends Annotation> Optional<T> getAnnotation(Method method, Class<T> annotationClass) {
        return Optional.ofNullable(method.getAnnotation(annotationClass));
    }

    public <T extends Annotation> Optional<T> getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        return Optional.ofNullable(clazz.getAnnotation(annotationClass));
    }

    public Iterable<Method> getNonStaticMethodsOnClass(Class<?> clazz) {
        return Stream.of(clazz.getMethods()).filter(method -> !Modifier.isStatic(method.getModifiers())).collect(Collectors.toList());
    }
}

