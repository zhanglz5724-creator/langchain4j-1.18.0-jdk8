/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.chat;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.jspecify.annotations.Nullable;

@Experimental
public interface BatchChatModel {
    public BatchResponse<ChatResponse> submit(BatchRequest<ChatRequest> var1);

    public BatchResponse<ChatResponse> retrieve(String var1);

    public void cancel(String var1);

    public BatchPage<ChatResponse> list(@Nullable BatchPagination var1);
}

