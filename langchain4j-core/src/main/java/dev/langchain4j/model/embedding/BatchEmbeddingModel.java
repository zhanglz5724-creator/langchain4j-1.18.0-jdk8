/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.embedding;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.output.Response;
import org.jspecify.annotations.Nullable;

@Experimental
public interface BatchEmbeddingModel {
    public BatchResponse<Response<Embedding>> submit(BatchRequest<TextSegment> var1);

    public BatchResponse<Response<Embedding>> retrieve(String var1);

    public void cancel(String var1);

    public BatchPage<Response<Embedding>> list(@Nullable BatchPagination var1);
}

