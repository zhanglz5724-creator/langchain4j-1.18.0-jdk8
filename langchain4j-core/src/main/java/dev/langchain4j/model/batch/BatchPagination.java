/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

@Experimental
public final class BatchPagination {
    private final @Nullable Integer pageSize;
    private final @Nullable String pageToken;

    public BatchPagination(@Nullable Integer pageSize, @Nullable String pageToken) {
        this.pageSize = pageSize;
        this.pageToken = pageToken;
    }

    public @Nullable Integer pageSize() {
        return this.pageSize;
    }

    public @Nullable String pageToken() {
        return this.pageToken;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BatchPagination that = (BatchPagination)o;
        return Objects.equals(this.pageSize, that.pageSize) && Objects.equals(this.pageToken, that.pageToken);
    }

    public int hashCode() {
        return Objects.hash(this.pageSize, this.pageToken);
    }

    public String toString() {
        return "BatchPagination{pageSize=" + this.pageSize + ", pageToken='" + this.pageToken + '\'' + '}';
    }
}

