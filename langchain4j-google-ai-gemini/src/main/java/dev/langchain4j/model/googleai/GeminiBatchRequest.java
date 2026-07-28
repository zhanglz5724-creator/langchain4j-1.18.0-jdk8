/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.batch.BatchRequest
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.model.batch.BatchRequest;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public class GeminiBatchRequest<T>
extends BatchRequest<T> {
    private final @Nullable String displayName;
    private final @Nullable Long priority;

    GeminiBatchRequest(List<T> requests, @Nullable String displayName, @Nullable Long priority) {
        super(requests);
        this.displayName = displayName;
        this.priority = priority;
    }

    public @Nullable String displayName() {
        return this.displayName;
    }

    public @Nullable Long priority() {
        return this.priority;
    }

    public static <T> GeminiBatchRequest<T> from(List<T> requests) {
        return new GeminiBatchRequest<T>(requests, null, null);
    }

    public static <T> GeminiBatchRequest<T> from(List<T> requests, String displayName) {
        return new GeminiBatchRequest<T>(requests, displayName, null);
    }

    public static <T> GeminiBatchRequest<T> from(List<T> requests, String displayName, Long priority) {
        return new GeminiBatchRequest<T>(requests, displayName, priority);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiBatchRequest)) {
            return false;
        }
        GeminiBatchRequest that = (GeminiBatchRequest)((Object)o);
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.displayName, that.displayName) && Objects.equals(this.priority, that.priority);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.displayName, this.priority);
    }

    public String toString() {
        return "GeminiBatchRequest{requests=" + this.requests() + ", displayName='" + this.displayName + '\'' + ", priority=" + this.priority + '}';
    }
}

