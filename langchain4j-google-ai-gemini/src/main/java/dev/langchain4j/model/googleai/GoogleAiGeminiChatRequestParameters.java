/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.util.Objects;

public class GoogleAiGeminiChatRequestParameters
extends DefaultChatRequestParameters {
    public static final GoogleAiGeminiChatRequestParameters EMPTY = GoogleAiGeminiChatRequestParameters.builder().build();
    private final String aspectRatio;
    private final String imageSize;
    private final String cachedContentName;

    private GoogleAiGeminiChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.aspectRatio = builder.aspectRatio;
        this.imageSize = builder.imageSize;
        this.cachedContentName = builder.cachedContentName;
    }

    public String aspectRatio() {
        return this.aspectRatio;
    }

    public String imageSize() {
        return this.imageSize;
    }

    public String cachedContentName() {
        return this.cachedContentName;
    }

    public GoogleAiGeminiChatRequestParameters overrideWith(ChatRequestParameters that) {
        return GoogleAiGeminiChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public GoogleAiGeminiChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return GoogleAiGeminiChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || ((Object)((Object)this)).getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GoogleAiGeminiChatRequestParameters that = (GoogleAiGeminiChatRequestParameters)((Object)o);
        return Objects.equals(this.aspectRatio, that.aspectRatio) && Objects.equals(this.imageSize, that.imageSize) && Objects.equals(this.cachedContentName, that.cachedContentName);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.aspectRatio, this.imageSize, this.cachedContentName);
    }

    public String toString() {
        return "GoogleAiGeminiChatRequestParameters{modelName=" + Utils.quoted((Object)this.modelName()) + ", temperature=" + this.temperature() + ", topP=" + this.topP() + ", topK=" + this.topK() + ", frequencyPenalty=" + this.frequencyPenalty() + ", presencePenalty=" + this.presencePenalty() + ", maxOutputTokens=" + this.maxOutputTokens() + ", stopSequences=" + this.stopSequences() + ", toolSpecifications=" + this.toolSpecifications() + ", toolChoice=" + this.toolChoice() + ", responseFormat=" + this.responseFormat() + ", aspectRatio=" + Utils.quoted((Object)this.aspectRatio) + ", imageSize=" + Utils.quoted((Object)this.imageSize) + ", cachedContentName=" + Utils.quoted((Object)this.cachedContentName) + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private String aspectRatio;
        private String imageSize;
        private String cachedContentName;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof GoogleAiGeminiChatRequestParameters) {
                GoogleAiGeminiChatRequestParameters geminiParameters = (GoogleAiGeminiChatRequestParameters)parameters;
                this.aspectRatio((String)Utils.getOrDefault((Object)geminiParameters.aspectRatio(), (Object)this.aspectRatio));
                this.imageSize((String)Utils.getOrDefault((Object)geminiParameters.imageSize(), (Object)this.imageSize));
                this.cachedContentName((String)Utils.getOrDefault((Object)geminiParameters.cachedContentName(), (Object)this.cachedContentName));
            }
            return this;
        }

        public Builder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder imageAspectRatio(String imageAspectRatio) {
            return this.aspectRatio(imageAspectRatio);
        }

        public Builder imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public Builder cachedContentName(String cachedContentName) {
            this.cachedContentName = cachedContentName;
            return this;
        }

        public GoogleAiGeminiChatRequestParameters build() {
            return new GoogleAiGeminiChatRequestParameters(this);
        }
    }
}

