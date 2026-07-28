/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.model.openai.internal.AsyncResponseHandling;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import java.util.function.Consumer;

public interface SyncOrAsync<ResponseContent> {
    public ResponseContent execute();

    default public ParsedAndRawResponse<ResponseContent> executeRaw() {
        ResponseContent parsedResponse = this.execute();
        SuccessfulHttpResponse rawHttpResponse = null;
        return new ParsedAndRawResponse<ResponseContent>(parsedResponse, rawHttpResponse);
    }

    public AsyncResponseHandling onResponse(Consumer<ResponseContent> var1);
}

