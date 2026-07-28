/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.google.genai;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.util.Objects;

public class GoogleGenAiChatRequestParameters
extends DefaultChatRequestParameters {
    public static final GoogleGenAiChatRequestParameters EMPTY = GoogleGenAiChatRequestParameters.builder().build();
    private final String cachedContent;

    private GoogleGenAiChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.cachedContent = builder.cachedContent;
    }

    public String cachedContent() {
        return this.cachedContent;
    }

    public GoogleGenAiChatRequestParameters overrideWith(ChatRequestParameters that) {
        return GoogleGenAiChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public GoogleGenAiChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return GoogleGenAiChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
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
        GoogleGenAiChatRequestParameters that = (GoogleGenAiChatRequestParameters)((Object)o);
        return Objects.equals(this.cachedContent, that.cachedContent);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.cachedContent);
    }

    public String toString() {
        return "GoogleGenAiChatRequestParameters{modelName=" + Utils.quoted((Object)this.modelName()) + ", temperature=" + this.temperature() + ", topP=" + this.topP() + ", topK=" + this.topK() + ", frequencyPenalty=" + this.frequencyPenalty() + ", presencePenalty=" + this.presencePenalty() + ", maxOutputTokens=" + this.maxOutputTokens() + ", stopSequences=" + this.stopSequences() + ", toolSpecifications=" + this.toolSpecifications() + ", toolChoice=" + this.toolChoice() + ", responseFormat=" + this.responseFormat() + ", cachedContent=" + Utils.quoted((Object)this.cachedContent) + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private String cachedContent;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof GoogleGenAiChatRequestParameters) {
                GoogleGenAiChatRequestParameters googleGenAiParameters = (GoogleGenAiChatRequestParameters)parameters;
                this.cachedContent((String)Utils.getOrDefault((Object)googleGenAiParameters.cachedContent(), (Object)this.cachedContent));
            }
            return this;
        }

        public Builder cachedContent(String cachedContent) {
            this.cachedContent = cachedContent;
            return this;
        }

        public GoogleGenAiChatRequestParameters build() {
            return new GoogleGenAiChatRequestParameters(this);
        }
    }
}

