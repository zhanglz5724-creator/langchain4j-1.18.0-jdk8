/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.batch.BatchResponse;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

@Experimental
public class BatchPage<T> {
    private final List<BatchResponse<T>> batches;
    private final @Nullable String nextPageToken;

    public BatchPage(List<BatchResponse<T>> batches, @Nullable String nextPageToken) {
        this.batches = Utils.copy(batches);
        this.nextPageToken = nextPageToken;
    }

    public List<BatchResponse<T>> batches() {
        return this.batches;
    }

    public @Nullable String nextPageToken() {
        return this.nextPageToken;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BatchPage batchPage = (BatchPage)o;
        return Objects.equals(this.batches, batchPage.batches) && Objects.equals(this.nextPageToken, batchPage.nextPageToken);
    }

    public int hashCode() {
        return Objects.hash(this.batches, this.nextPageToken);
    }

    public String toString() {
        return "BatchPage{batches=" + this.batches + ", nextPageToken='" + this.nextPageToken + '\'' + '}';
    }
}

