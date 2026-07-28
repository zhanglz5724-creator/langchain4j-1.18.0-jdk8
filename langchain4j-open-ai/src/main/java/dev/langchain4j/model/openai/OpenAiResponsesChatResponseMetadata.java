/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.output.TokenUsage;
import java.util.List;
import java.util.Objects;

public class OpenAiResponsesChatResponseMetadata
extends ChatResponseMetadata {
    private final Long createdAt;
    private final Long completedAt;
    private final String serviceTier;
    private final SuccessfulHttpResponse rawHttpResponse;
    private final List<ServerSentEvent> rawServerSentEvents;

    private OpenAiResponsesChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.createdAt = builder.createdAt;
        this.completedAt = builder.completedAt;
        this.serviceTier = builder.serviceTier;
        this.rawHttpResponse = builder.rawHttpResponse;
        this.rawServerSentEvents = Utils.copy((List)builder.rawServerSentEvents);
    }

    public OpenAiTokenUsage tokenUsage() {
        TokenUsage base = super.tokenUsage();
        if (base == null) {
            return null;
        }
        if (base instanceof OpenAiTokenUsage) {
            return (OpenAiTokenUsage)base;
        }
        return OpenAiTokenUsage.builder().inputTokenCount(base.inputTokenCount()).outputTokenCount(base.outputTokenCount()).totalTokenCount(base.totalTokenCount()).build();
    }

    public Long createdAt() {
        return this.createdAt;
    }

    public Long completedAt() {
        return this.completedAt;
    }

    public String serviceTier() {
        return this.serviceTier;
    }

    public SuccessfulHttpResponse rawHttpResponse() {
        return this.rawHttpResponse;
    }

    public List<ServerSentEvent> rawServerSentEvents() {
        return this.rawServerSentEvents;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)OpenAiResponsesChatResponseMetadata.builder())).createdAt(this.createdAt).completedAt(this.completedAt).serviceTier(this.serviceTier).rawHttpResponse(this.rawHttpResponse).rawServerSentEvents(this.rawServerSentEvents);
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
        OpenAiResponsesChatResponseMetadata that = (OpenAiResponsesChatResponseMetadata)((Object)o);
        return Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.completedAt, that.completedAt) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.rawHttpResponse, that.rawHttpResponse) && Objects.equals(this.rawServerSentEvents, that.rawServerSentEvents);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.createdAt, this.completedAt, this.serviceTier, this.rawHttpResponse, this.rawServerSentEvents);
    }

    public String toString() {
        return "OpenAiResponsesChatResponseMetadata{id='" + this.id() + '\'' + ", modelName='" + this.modelName() + '\'' + ", tokenUsage=" + (Object)((Object)this.tokenUsage()) + ", finishReason=" + this.finishReason() + ", createdAt=" + this.createdAt + ", completedAt=" + this.completedAt + ", serviceTier='" + this.serviceTier + '\'' + ", rawHttpResponse=" + this.rawHttpResponse + ", rawServerSentEvents=" + this.rawServerSentEvents + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private Long createdAt;
        private Long completedAt;
        private String serviceTier;
        private SuccessfulHttpResponse rawHttpResponse;
        private List<ServerSentEvent> rawServerSentEvents;

        public Builder createdAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder completedAt(Long completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder rawHttpResponse(SuccessfulHttpResponse rawHttpResponse) {
            this.rawHttpResponse = rawHttpResponse;
            return this;
        }

        public Builder rawServerSentEvents(List<ServerSentEvent> rawServerSentEvents) {
            this.rawServerSentEvents = rawServerSentEvents;
            return this;
        }

        public OpenAiResponsesChatResponseMetadata build() {
            return new OpenAiResponsesChatResponseMetadata(this);
        }
    }
}

