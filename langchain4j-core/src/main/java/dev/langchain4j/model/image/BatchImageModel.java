/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.image;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.batch.BatchPage;
import dev.langchain4j.model.batch.BatchPagination;
import dev.langchain4j.model.batch.BatchRequest;
import dev.langchain4j.model.batch.BatchResponse;
import dev.langchain4j.model.output.Response;
import org.jspecify.annotations.Nullable;

@Experimental
public interface BatchImageModel {
    public BatchResponse<Response<Image>> submit(BatchRequest<String> var1);

    public BatchResponse<Response<Image>> retrieve(String var1);

    public void cancel(String var1);

    public BatchPage<Response<Image>> list(@Nullable BatchPagination var1);
}

