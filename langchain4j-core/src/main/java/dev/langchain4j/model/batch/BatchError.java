/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

@Experimental
public final class BatchError {
    private final int code;
    private final String message;
    private final @Nullable List<Map<String, Object>> details;

    public BatchError(int code, String message, @Nullable List<Map<String, Object>> details) {
        this.code = code;
        this.message = message;
        this.details = Utils.copy(details);
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public @Nullable List<Map<String, Object>> details() {
        return this.details;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BatchError that = (BatchError)o;
        return this.code == that.code && Objects.equals(this.message, that.message) && Objects.equals(this.details, that.details);
    }

    public int hashCode() {
        return Objects.hash(this.code, this.message, this.details);
    }

    public String toString() {
        return "BatchError{code=" + this.code + ", message='" + this.message + '\'' + ", details=" + this.details + '}';
    }
}

