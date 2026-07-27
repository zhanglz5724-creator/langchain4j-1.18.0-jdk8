/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.output;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class Response<T> {
    private final T content;
    private final @Nullable TokenUsage tokenUsage;
    private final @Nullable FinishReason finishReason;
    private final Map<String, Object> metadata;

    public Response(T content) {
        this(content, null, null, Collections.emptyMap());
    }

    public Response(T content, @Nullable TokenUsage tokenUsage, @Nullable FinishReason finishReason) {
        this(content, tokenUsage, finishReason, Collections.emptyMap());
    }

    public Response(T content, @Nullable TokenUsage tokenUsage, @Nullable FinishReason finishReason, @Nullable Map<String, Object> metadata) {
        this.content = ValidationUtils.ensureNotNull(content, "content");
        this.tokenUsage = tokenUsage;
        this.finishReason = finishReason;
        this.metadata = Utils.copy(metadata);
    }

    public T content() {
        return this.content;
    }

    public @Nullable TokenUsage tokenUsage() {
        return this.tokenUsage;
    }

    public @Nullable FinishReason finishReason() {
        return this.finishReason;
    }

    public Map<String, Object> metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Response that = (Response)o;
        return Objects.equals(this.content, that.content) && Objects.equals(this.tokenUsage, that.tokenUsage) && Objects.equals((Object)this.finishReason, (Object)that.finishReason) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.content, this.tokenUsage, this.finishReason, this.metadata});
    }

    public String toString() {
        return "Response { content = " + this.content + ", tokenUsage = " + this.tokenUsage + ", finishReason = " + (Object)((Object)this.finishReason) + ", metadata = " + this.metadata + " }";
    }

    public static <T> Response<T> from(T content) {
        return new Response<T>(content);
    }

    public static <T> Response<T> from(T content, @Nullable TokenUsage tokenUsage) {
        return new Response<T>(content, tokenUsage, null);
    }

    public static <T> Response<T> from(T content, @Nullable TokenUsage tokenUsage, @Nullable FinishReason finishReason) {
        return new Response<T>(content, tokenUsage, finishReason);
    }

    public static <T> Response<T> from(T content, @Nullable TokenUsage tokenUsage, @Nullable FinishReason finishReason, @Nullable Map<String, Object> metadata) {
        return new Response<T>(content, tokenUsage, finishReason, metadata);
    }
}

