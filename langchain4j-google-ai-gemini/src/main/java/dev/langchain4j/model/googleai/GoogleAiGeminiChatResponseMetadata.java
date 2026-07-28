/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.googleai.GroundingMetadata;
import dev.langchain4j.model.googleai.UrlContextMetadata;
import java.util.Objects;

public class GoogleAiGeminiChatResponseMetadata
extends ChatResponseMetadata {
    private final GroundingMetadata groundingMetadata;
    private final UrlContextMetadata urlContextMetadata;

    private GoogleAiGeminiChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.groundingMetadata = builder.groundingMetadata;
        this.urlContextMetadata = builder.urlContextMetadata;
    }

    public GroundingMetadata groundingMetadata() {
        return this.groundingMetadata;
    }

    public UrlContextMetadata urlContextMetadata() {
        return this.urlContextMetadata;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)GoogleAiGeminiChatResponseMetadata.builder())).groundingMetadata(this.groundingMetadata).urlContextMetadata(this.urlContextMetadata);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoogleAiGeminiChatResponseMetadata)) {
            return false;
        }
        GoogleAiGeminiChatResponseMetadata that = (GoogleAiGeminiChatResponseMetadata)((Object)o);
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.groundingMetadata, that.groundingMetadata) && Objects.equals(this.urlContextMetadata, that.urlContextMetadata);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.groundingMetadata, this.urlContextMetadata);
    }

    public String toString() {
        return "GoogleAiGeminiChatResponseMetadata{id='" + this.id() + '\'' + ", modelName='" + this.modelName() + '\'' + ", tokenUsage=" + this.tokenUsage() + ", finishReason=" + this.finishReason() + ", groundingMetadata=" + this.groundingMetadata + ", urlContextMetadata=" + this.urlContextMetadata + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private GroundingMetadata groundingMetadata;
        private UrlContextMetadata urlContextMetadata;

        public Builder groundingMetadata(GroundingMetadata groundingMetadata) {
            this.groundingMetadata = groundingMetadata;
            return this;
        }

        public Builder urlContextMetadata(UrlContextMetadata urlContextMetadata) {
            this.urlContextMetadata = urlContextMetadata;
            return this;
        }

        public GoogleAiGeminiChatResponseMetadata build() {
            return new GoogleAiGeminiChatResponseMetadata(this);
        }
    }
}

