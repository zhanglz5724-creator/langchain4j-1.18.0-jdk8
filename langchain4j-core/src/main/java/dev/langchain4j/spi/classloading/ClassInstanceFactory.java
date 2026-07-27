/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.classloading;

public interface ClassInstanceFactory {
    public <T> T getInstanceOfClass(Class<T> var1);
}

