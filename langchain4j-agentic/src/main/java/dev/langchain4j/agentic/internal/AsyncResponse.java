/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.DefaultExecutorProvider
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.DelayedResponse;
import dev.langchain4j.internal.DefaultExecutorProvider;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AsyncResponse<T>
implements DelayedResponse<T> {
    private final CompletableFuture<T> futureResponse;

    public AsyncResponse(Supplier<T> responseSupplier) {
        this.futureResponse = CompletableFuture.supplyAsync(responseSupplier, DefaultExecutorProvider.getDefaultExecutorService());
    }

    @Override
    public boolean isDone() {
        return this.futureResponse.isDone();
    }

    @Override
    public T blockingGet() {
        return DelayedResponse.join(this.futureResponse);
    }

    public String toString() {
        return this.result().toString();
    }
}

