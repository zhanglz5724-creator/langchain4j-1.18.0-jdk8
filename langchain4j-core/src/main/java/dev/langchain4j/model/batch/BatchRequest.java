/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;

@Experimental
public class BatchRequest<T> {
    private final List<T> requests;

    public BatchRequest(List<T> requests) {
        this.requests = Utils.copy(ValidationUtils.ensureNotNull(requests, "requests"));
    }

    public List<T> requests() {
        return this.requests;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BatchRequest that = (BatchRequest)o;
        return Objects.equals(this.requests, that.requests);
    }

    public int hashCode() {
        return Objects.hashCode(this.requests);
    }

    public String toString() {
        return "BatchRequest{requests=" + this.requests + '}';
    }
}

