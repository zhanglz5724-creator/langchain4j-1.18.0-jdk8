/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import java.util.List;
import java.util.Objects;

public class MistralAiChatResponseMetadata
extends ChatResponseMetadata {
    private final SuccessfulHttpResponse rawHttpResponse;
    private final List<ServerSentEvent> rawServerSentEvents;

    private MistralAiChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.rawHttpResponse = builder.rawHttpResponse;
        this.rawServerSentEvents = Utils.copy((List)builder.rawServerSentEvents);
    }

    public SuccessfulHttpResponse rawHttpResponse() {
        return this.rawHttpResponse;
    }

    public List<ServerSentEvent> rawServerSentEvents() {
        return this.rawServerSentEvents;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)MistralAiChatResponseMetadata.builder())).rawHttpResponse(this.rawHttpResponse).rawServerSentEvents(this.rawServerSentEvents);
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
        MistralAiChatResponseMetadata that = (MistralAiChatResponseMetadata)((Object)o);
        return Objects.equals(this.rawHttpResponse, that.rawHttpResponse) && Objects.equals(this.rawServerSentEvents, that.rawServerSentEvents);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.rawHttpResponse, this.rawServerSentEvents);
    }

    public String toString() {
        return "MistralAiChatResponseMetadata{id='" + this.id() + '\'' + ", modelName='" + this.modelName() + '\'' + ", tokenUsage=" + this.tokenUsage() + ", finishReason=" + this.finishReason() + ", created=" + this.rawHttpResponse + ", rawServerSentEvents=" + this.rawServerSentEvents + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private SuccessfulHttpResponse rawHttpResponse;
        private List<ServerSentEvent> rawServerSentEvents;

        public Builder rawHttpResponse(SuccessfulHttpResponse rawHttpResponse) {
            this.rawHttpResponse = rawHttpResponse;
            return this;
        }

        public Builder rawServerSentEvents(List<ServerSentEvent> rawServerSentEvents) {
            this.rawServerSentEvents = rawServerSentEvents;
            return this;
        }

        public MistralAiChatResponseMetadata build() {
            return new MistralAiChatResponseMetadata(this);
        }
    }
}

