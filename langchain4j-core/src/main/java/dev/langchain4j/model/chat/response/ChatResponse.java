/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Objects;

public class ChatResponse {
    private final AiMessage aiMessage;
    private final ChatResponseMetadata metadata;

    protected ChatResponse(Builder builder) {
        this.aiMessage = ValidationUtils.ensureNotNull(builder.aiMessage, "aiMessage");
        ChatResponseMetadata.Builder<?> metadataBuilder = ChatResponseMetadata.builder();
        if (builder.id != null) {
            ChatResponse.validate(builder, "id");
            metadataBuilder.id(builder.id);
        }
        if (builder.modelName != null) {
            ChatResponse.validate(builder, "modelName");
            metadataBuilder.modelName(builder.modelName);
        }
        if (builder.tokenUsage != null) {
            ChatResponse.validate(builder, "tokenUsage");
            metadataBuilder.tokenUsage(builder.tokenUsage);
        }
        if (builder.finishReason != null) {
            ChatResponse.validate(builder, "finishReason");
            metadataBuilder.finishReason(builder.finishReason);
        }
        this.metadata = builder.metadata != null ? builder.metadata : metadataBuilder.build();
    }

    public AiMessage aiMessage() {
        return this.aiMessage;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public ChatResponseMetadata metadata() {
        return this.metadata;
    }

    public String id() {
        return this.metadata.id();
    }

    public String modelName() {
        return this.metadata.modelName();
    }

    public TokenUsage tokenUsage() {
        return this.metadata.tokenUsage();
    }

    public FinishReason finishReason() {
        return this.metadata.finishReason();
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChatResponse that = (ChatResponse)o;
        return Objects.equals(this.aiMessage, that.aiMessage) && Objects.equals(this.metadata, that.metadata);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.aiMessage, this.metadata);
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ChatResponse { aiMessage = " + this.aiMessage + ", metadata = " + this.metadata + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    private static void validate(Builder builder, String name) {
        if (builder.metadata != null) {
            throw new IllegalArgumentException(String.format("Cannot set both 'metadata' and '%s' on ChatResponse", name));
        }
    }

    public static class Builder {
        private AiMessage aiMessage;
        private ChatResponseMetadata metadata;
        private String id;
        private String modelName;
        private TokenUsage tokenUsage;
        private FinishReason finishReason;

        public Builder() {
        }

        public Builder(ChatResponse chatResponse) {
            this.aiMessage = chatResponse.aiMessage;
            this.metadata = chatResponse.metadata;
        }

        public Builder aiMessage(AiMessage aiMessage) {
            this.aiMessage = aiMessage;
            return this;
        }

        public Builder metadata(ChatResponseMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder tokenUsage(TokenUsage tokenUsage) {
            this.tokenUsage = tokenUsage;
            return this;
        }

        public Builder finishReason(FinishReason finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public ChatResponse build() {
            return new ChatResponse(this);
        }
    }
}

