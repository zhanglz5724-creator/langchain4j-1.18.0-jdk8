/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.batch.BatchError;
import dev.langchain4j.model.batch.BatchItemResult;
import dev.langchain4j.model.batch.BatchState;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Experimental
public class BatchResponse<T> {
    private final String batchId;
    private final BatchState state;
    private final List<BatchItemResult<T>> results;

    public BatchResponse(Builder<T> builder) {
        this.batchId = ValidationUtils.ensureNotBlank(((Builder)builder).batchId, "batchId");
        this.state = ValidationUtils.ensureNotNull(((Builder)builder).state, "state");
        this.results = Utils.copy(((Builder)builder).results);
    }

    public String batchId() {
        return this.batchId;
    }

    public BatchState state() {
        return this.state;
    }

    public List<BatchItemResult<T>> results() {
        return this.results;
    }

    public List<T> responses() {
        return this.results.stream().filter(BatchItemResult::isSuccess).map(BatchItemResult::response).collect(Collectors.toList());
    }

    public List<BatchError> errors() {
        return this.results.stream().filter(result -> !result.isSuccess()).map(BatchItemResult::error).collect(Collectors.toList());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BatchResponse that = (BatchResponse)o;
        return Objects.equals(this.batchId, that.batchId) && this.state == that.state && Objects.equals(this.results, that.results);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.batchId, this.state, this.results});
    }

    public String toString() {
        return "BatchResponse{batchId=" + this.batchId + ", state=" + (Object)((Object)this.state) + ", results=" + this.results + '}';
    }

    public static <T> Builder<T> builder() {
        return new Builder();
    }

    public static class Builder<T> {
        private String batchId;
        private BatchState state;
        private List<BatchItemResult<T>> results;

        public Builder<T> batchId(String batchId) {
            this.batchId = batchId;
            return this;
        }

        public Builder<T> state(BatchState state) {
            this.state = state;
            return this;
        }

        public Builder<T> results(List<BatchItemResult<T>> results) {
            this.results = results;
            return this;
        }

        public BatchResponse<T> build() {
            return new BatchResponse(this);
        }
    }
}

