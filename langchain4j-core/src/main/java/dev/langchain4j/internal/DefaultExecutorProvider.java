/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.VirtualThreadUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Internal
public class DefaultExecutorProvider {
    private DefaultExecutorProvider() {
    }

    public static ExecutorService getDefaultExecutorService() {
        return Holder.EXECUTOR_SERVICE;
    }

    private static class Holder {
        private static final ExecutorService EXECUTOR_SERVICE = VirtualThreadUtils.createVirtualThreadExecutor(Holder::createPlatformThreadExecutorService);

        private Holder() {
        }

        private static ExecutorService createPlatformThreadExecutorService() {
            return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        }
    }
}

