/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface DelayedResponse<T> {
    public boolean isDone();

    public T blockingGet();

    default public Object result() {
        return this.isDone() ? this.blockingGet() : "<pending>";
    }

    public static <R> R join(CompletableFuture<R> future) {
        try {
            return future.join();
        }
        catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            if (cause instanceof Error) {
                throw (Error)cause;
            }
            throw e;
        }
    }
}

