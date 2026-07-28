/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.genai.types.GenerateContentResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.google.genai;

import com.google.genai.types.GenerateContentResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import java.util.Objects;

public class GoogleGenAiChatResponseMetadata
extends ChatResponseMetadata {
    private final GenerateContentResponse rawResponse;

    private GoogleGenAiChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.rawResponse = builder.rawResponse;
    }

    public GenerateContentResponse rawResponse() {
        return this.rawResponse;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)GoogleGenAiChatResponseMetadata.builder())).rawResponse(this.rawResponse);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoogleGenAiChatResponseMetadata)) {
            return false;
        }
        GoogleGenAiChatResponseMetadata that = (GoogleGenAiChatResponseMetadata)((Object)o);
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.rawResponse, that.rawResponse);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.rawResponse);
    }

    public String toString() {
        return "GoogleGenAiChatResponseMetadata{id='" + this.id() + '\'' + ", modelName='" + this.modelName() + '\'' + ", tokenUsage=" + this.tokenUsage() + ", finishReason=" + this.finishReason() + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private GenerateContentResponse rawResponse;

        public Builder rawResponse(GenerateContentResponse rawResponse) {
            this.rawResponse = rawResponse;
            return this;
        }

        public GoogleGenAiChatResponseMetadata build() {
            return new GoogleGenAiChatResponseMetadata(this);
        }
    }
}

