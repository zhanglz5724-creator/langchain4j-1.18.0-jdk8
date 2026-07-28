/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.spi.classloading.ClassMetadataProviderFactory
 */
package dev.langchain4j.classloading;

import dev.langchain4j.classloading.ReflectionBasedClassMetadataProviderFactory;
import dev.langchain4j.spi.classloading.ClassMetadataProviderFactory;
import java.util.ServiceLoader;

public final class ClassMetadataProvider {
    private static final ReflectionBasedClassMetadataProviderFactory DEFAULT_CLASS_METADATA_PROVIDER_FACTORY = new ReflectionBasedClassMetadataProviderFactory();

    private ClassMetadataProvider() {
    }

    public static <MethodKey> ClassMetadataProviderFactory<MethodKey> getClassMetadataProviderFactory() {
        for (ClassMetadataProviderFactory factory : ServiceLoader.load(ClassMetadataProviderFactory.class)) {
            if (DEFAULT_CLASS_METADATA_PROVIDER_FACTORY.getClass().equals(factory.getClass())) continue;
            return factory;
        }
        return DEFAULT_CLASS_METADATA_PROVIDER_FACTORY;
    }
}

