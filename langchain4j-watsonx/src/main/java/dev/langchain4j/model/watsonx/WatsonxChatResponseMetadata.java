/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata$Builder
 */
package dev.langchain4j.model.watsonx;

import dev.langchain4j.model.chat.response.ChatResponseMetadata;

public class WatsonxChatResponseMetadata
extends ChatResponseMetadata {
    private final Long created;
    private final String modelVersion;

    private WatsonxChatResponseMetadata(Builder builder) {
        super((ChatResponseMetadata.Builder)builder);
        this.created = builder.created;
        this.modelVersion = builder.modelVersion;
    }

    public Long getCreated() {
        return this.created;
    }

    public String getModelVersion() {
        return this.modelVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends ChatResponseMetadata.Builder<Builder> {
        private Long created;
        private String modelVersion;

        public Builder created(Long created) {
            this.created = created;
            return this;
        }

        public Builder modelVersion(String modelVersion) {
            this.modelVersion = modelVersion;
            return this;
        }

        public ChatResponseMetadata build() {
            return new WatsonxChatResponseMetadata(this);
        }
    }
}

