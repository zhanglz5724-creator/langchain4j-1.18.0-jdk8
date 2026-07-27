/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.classloading;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface ClassMetadataProviderFactory<MethodKey> {
    public <T extends Annotation> Optional<T> getAnnotation(MethodKey var1, Class<T> var2);

    public <T extends Annotation> Optional<T> getAnnotation(Class<?> var1, Class<T> var2);

    public Iterable<MethodKey> getNonStaticMethodsOnClass(Class<?> var1);
}

