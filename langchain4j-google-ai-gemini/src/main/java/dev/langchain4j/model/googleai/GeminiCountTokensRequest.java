/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.internal.Utils
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiCountTokensRequest {
    private final @Nullable List<GeminiContent> contents;
    private final @Nullable GeminiGenerateContentRequest generateContentRequest;

    @JsonCreator
    GeminiCountTokensRequest(@JsonProperty(value="contents") @Nullable List<GeminiContent> contents, @JsonProperty(value="generateContentRequest") @Nullable GeminiGenerateContentRequest generateContentRequest) {
        if (Utils.isNullOrEmpty(contents) && generateContentRequest == null) {
            throw new IllegalArgumentException("Either contents or generateContentRequest should be set");
        }
        this.contents = contents;
        this.generateContentRequest = generateContentRequest;
    }

    @Nullable List<GeminiContent> contents() {
        return this.contents;
    }

    @Nullable GeminiGenerateContentRequest generateContentRequest() {
        return this.generateContentRequest;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiCountTokensRequest)) {
            return false;
        }
        GeminiCountTokensRequest that = (GeminiCountTokensRequest)o;
        return Objects.equals(this.contents, that.contents) && Objects.equals(this.generateContentRequest, that.generateContentRequest);
    }

    public int hashCode() {
        return Objects.hash(this.contents, this.generateContentRequest);
    }

    public String toString() {
        return "GeminiCountTokensRequest[contents=" + this.contents + ", generateContentRequest=" + this.generateContentRequest + "]";
    }
}

