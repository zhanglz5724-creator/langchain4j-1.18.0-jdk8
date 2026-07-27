/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Exceptions;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

@Internal
public class VirtualThreadUtils {
    private static @Nullable Method newVirtualThreadPerTaskExecutorMethod;
    private static @Nullable Method isVirtualMethod;

    public static @Nullable ExecutorService createVirtualThreadExecutor(Supplier<@Nullable ExecutorService> fallback) {
        try {
            if (newVirtualThreadPerTaskExecutorMethod != null) {
                return (ExecutorService)newVirtualThreadPerTaskExecutorMethod.invoke(null, new Object[0]);
            }
        }
        catch (Exception e) {
            throw Exceptions.runtime("Failed to create virtual thread executor", e);
        }
        if (fallback == null) {
            return null;
        }
        return fallback.get();
    }

    public static ExecutorService createVirtualThreadExecutor() {
        if (VirtualThreadUtils.isVirtualThreadsSupported()) {
            return VirtualThreadUtils.createVirtualThreadExecutor(null);
        }
        throw Exceptions.runtime("Virtual threads not supported", new Object[0]);
    }

    public static boolean isVirtualThread() {
        try {
            if (isVirtualMethod != null) {
                return (Boolean)isVirtualMethod.invoke(Thread.currentThread(), new Object[0]);
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isVirtualThreadsSupported() {
        return newVirtualThreadPerTaskExecutorMethod != null;
    }

    static {
        try {
            newVirtualThreadPerTaskExecutorMethod = Executors.class.getMethod("newVirtualThreadPerTaskExecutor", new Class[0]);
            isVirtualMethod = Thread.class.getMethod("isVirtual", new Class[0]);
        }
        catch (Exception e) {
            newVirtualThreadPerTaskExecutorMethod = null;
            isVirtualMethod = null;
        }
    }
}

