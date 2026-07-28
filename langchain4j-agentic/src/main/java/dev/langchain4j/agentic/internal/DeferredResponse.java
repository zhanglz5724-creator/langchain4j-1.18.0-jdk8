/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.agentic.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.agentic.internal.DelayedResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class DeferredResponse<T>
implements DelayedResponse<T> {
    private final String responseId;
    @JsonIgnore
    private transient CompletableFuture<T> futureResponse;

    protected DeferredResponse(@JsonProperty(value="responseId") String responseId) {
        this.responseId = responseId;
        this.futureResponse = new CompletableFuture();
    }

    public String responseId() {
        return this.responseId;
    }

    @Override
    @JsonIgnore
    public boolean isDone() {
        return this.futureResponse.isDone();
    }

    @Override
    @JsonIgnore
    public T blockingGet() {
        return DelayedResponse.join(this.futureResponse);
    }

    public T blockingGet(long timeout, TimeUnit unit) throws TimeoutException {
        try {
            return this.futureResponse.get(timeout, unit);
        }
        catch (TimeoutException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean complete(T value) {
        return this.futureResponse.complete(value);
    }

    public boolean completeExceptionally(Throwable exception) {
        return this.futureResponse.completeExceptionally(exception);
    }

    public String toString() {
        return this.isDone() ? String.valueOf(this.result()) : "<pending:" + this.responseId + ">";
    }
}

