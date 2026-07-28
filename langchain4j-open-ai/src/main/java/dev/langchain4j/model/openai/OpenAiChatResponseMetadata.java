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
import dev.langchain4j.model.openai.LogProb;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.output.TokenUsage;
import java.util.List;
import java.util.Objects;

public class OpenAiChatResponseMetadata
extends ChatResponseMetadata {
    private final Long created;
    private final String serviceTier;
    private final String systemFingerprint;
    private final SuccessfulHttpResponse rawHttpResponse;
    private final List<ServerSentEvent> rawServerSentEvents;
    private final List<LogProb> logProbs;

    private OpenAiChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.created = builder.created;
        this.serviceTier = builder.serviceTier;
        this.systemFingerprint = builder.systemFingerprint;
        this.rawHttpResponse = builder.rawHttpResponse;
        this.rawServerSentEvents = Utils.copy((List)builder.rawServerSentEvents);
        this.logProbs = builder.logProbs;
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

    public Long created() {
        return this.created;
    }

    public String serviceTier() {
        return this.serviceTier;
    }

    public String systemFingerprint() {
        return this.systemFingerprint;
    }

    public SuccessfulHttpResponse rawHttpResponse() {
        return this.rawHttpResponse;
    }

    public List<ServerSentEvent> rawServerSentEvents() {
        return this.rawServerSentEvents;
    }

    public List<LogProb> logProbs() {
        return this.logProbs;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)OpenAiChatResponseMetadata.builder())).created(this.created).serviceTier(this.serviceTier).systemFingerprint(this.systemFingerprint).rawHttpResponse(this.rawHttpResponse).rawServerSentEvents(this.rawServerSentEvents).logProbs(this.logProbs);
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
        OpenAiChatResponseMetadata that = (OpenAiChatResponseMetadata)((Object)o);
        return Objects.equals(this.created, that.created) && Objects.equals(this.serviceTier, that.serviceTier) && Objects.equals(this.systemFingerprint, that.systemFingerprint) && Objects.equals(this.rawHttpResponse, that.rawHttpResponse) && Objects.equals(this.rawServerSentEvents, that.rawServerSentEvents) && Objects.equals(this.logProbs, that.logProbs);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.created, this.serviceTier, this.systemFingerprint, this.rawHttpResponse, this.rawServerSentEvents, this.logProbs);
    }

    public String toString() {
        return "OpenAiChatResponseMetadata{id='" + this.id() + '\'' + ", modelName='" + this.modelName() + '\'' + ", tokenUsage=" + (Object)((Object)this.tokenUsage()) + ", finishReason=" + this.finishReason() + ", created=" + this.created + ", serviceTier='" + this.serviceTier + '\'' + ", systemFingerprint='" + this.systemFingerprint + '\'' + ", rawHttpResponse=" + this.rawHttpResponse + ", rawServerSentEvents=" + this.rawServerSentEvents + ", logProbs=" + this.logProbs + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private Long created;
        private String serviceTier;
        private String systemFingerprint;
        private SuccessfulHttpResponse rawHttpResponse;
        private List<ServerSentEvent> rawServerSentEvents;
        private List<LogProb> logProbs;

        public Builder created(Long created) {
            this.created = created;
            return this;
        }

        public Builder serviceTier(String serviceTier) {
            this.serviceTier = serviceTier;
            return this;
        }

        public Builder systemFingerprint(String systemFingerprint) {
            this.systemFingerprint = systemFingerprint;
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

        public Builder logProbs(List<LogProb> logProbs) {
            this.logProbs = logProbs;
            return this;
        }

        public OpenAiChatResponseMetadata build() {
            return new OpenAiChatResponseMetadata(this);
        }
    }
}

