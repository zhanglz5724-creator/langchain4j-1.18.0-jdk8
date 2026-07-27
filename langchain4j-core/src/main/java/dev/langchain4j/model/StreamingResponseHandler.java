/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model;

import dev.langchain4j.model.output.Response;

public interface StreamingResponseHandler<T> {
    public void onNext(String var1);

    default public void onComplete(Response<T> response) {
    }

    public void onError(Throwable var1);
}

