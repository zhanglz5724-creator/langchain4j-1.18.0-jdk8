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
package dev.langchain4j.model.anthropic;

import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.anthropic.AnthropicCacheDiagnostics;
import dev.langchain4j.model.anthropic.AnthropicTokenUsage;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import java.util.List;
import java.util.Objects;

public class AnthropicChatResponseMetadata
extends ChatResponseMetadata {
    private final SuccessfulHttpResponse rawHttpResponse;
    private final List<ServerSentEvent> rawServerSentEvents;
    private final AnthropicCacheDiagnostics cacheDiagnostics;

    private AnthropicChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.rawHttpResponse = builder.rawHttpResponse;
        this.rawServerSentEvents = Utils.copy((List)builder.rawServerSentEvents);
        this.cacheDiagnostics = builder.cacheDiagnostics;
    }

    public AnthropicTokenUsage tokenUsage() {
        return (AnthropicTokenUsage)super.tokenUsage();
    }

    public SuccessfulHttpResponse rawHttpResponse() {
        return this.rawHttpResponse;
    }

    public List<ServerSentEvent> rawServerSentEvents() {
        return this.rawServerSentEvents;
    }

    public AnthropicCacheDiagnostics cacheDiagnostics() {
        return this.cacheDiagnostics;
    }

    public Builder toBuilder() {
        return ((Builder)super.toBuilder((ChatResponseMetadata.Builder)AnthropicChatResponseMetadata.builder())).rawHttpResponse(this.rawHttpResponse).rawServerSentEvents(this.rawServerSentEvents).cacheDiagnostics(this.cacheDiagnostics);
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
        AnthropicChatResponseMetadata that = (AnthropicChatResponseMetadata)((Object)o);
        return Objects.equals(this.rawHttpResponse, that.rawHttpResponse) && Objects.equals(this.rawServerSentEvents, that.rawServerSentEvents) && Objects.equals(this.cacheDiagnostics, that.cacheDiagnostics);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.rawHttpResponse, this.rawServerSentEvents, this.cacheDiagnostics);
    }

    public String toString() {
        return "AnthropicChatResponseMetadata{id='" + this.id() + '\'' + ", modelName='" + this.modelName() + '\'' + ", tokenUsage=" + (Object)((Object)this.tokenUsage()) + ", finishReason=" + this.finishReason() + ", created=" + this.rawHttpResponse + ", rawServerSentEvents=" + this.rawServerSentEvents + ", cacheDiagnostics=" + this.cacheDiagnostics + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private SuccessfulHttpResponse rawHttpResponse;
        private List<ServerSentEvent> rawServerSentEvents;
        private AnthropicCacheDiagnostics cacheDiagnostics;

        public Builder rawHttpResponse(SuccessfulHttpResponse rawHttpResponse) {
            this.rawHttpResponse = rawHttpResponse;
            return this;
        }

        public Builder rawServerSentEvents(List<ServerSentEvent> rawServerSentEvents) {
            this.rawServerSentEvents = rawServerSentEvents;
            return this;
        }

        public Builder cacheDiagnostics(AnthropicCacheDiagnostics cacheDiagnostics) {
            this.cacheDiagnostics = cacheDiagnostics;
            return this;
        }

        public AnthropicChatResponseMetadata build() {
            return new AnthropicChatResponseMetadata(this);
        }
    }
}

