/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import java.util.Objects;

@JacocoIgnoreCoverageGenerated
public class ChatResponseMetadata {
    private final String id;
    private final String modelName;
    private final TokenUsage tokenUsage;
    private final FinishReason finishReason;

    protected ChatResponseMetadata(Builder<?> builder) {
        this.id = ((Builder)builder).id;
        this.modelName = ((Builder)builder).modelName;
        this.tokenUsage = ((Builder)builder).tokenUsage;
        this.finishReason = ((Builder)builder).finishReason;
    }

    public String id() {
        return this.id;
    }

    public String modelName() {
        return this.modelName;
    }

    public TokenUsage tokenUsage() {
        return this.tokenUsage;
    }

    public FinishReason finishReason() {
        return this.finishReason;
    }

    public Builder<?> toBuilder() {
        return this.toBuilder(ChatResponseMetadata.builder());
    }

    protected Builder<?> toBuilder(Builder<?> builder) {
        return ((Builder)((Builder)((Builder)builder.id(this.id)).modelName(this.modelName)).tokenUsage(this.tokenUsage)).finishReason(this.finishReason);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChatResponseMetadata that = (ChatResponseMetadata)o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.modelName, that.modelName) && Objects.equals(this.tokenUsage, that.tokenUsage) && Objects.equals((Object)this.finishReason, (Object)that.finishReason);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.modelName, this.tokenUsage, this.finishReason});
    }

    public String toString() {
        return "ChatResponseMetadata{id='" + this.id + '\'' + ", modelName='" + this.modelName + '\'' + ", tokenUsage=" + this.tokenUsage + ", finishReason=" + (Object)((Object)this.finishReason) + '}';
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder<T>> {
        private String id;
        private String modelName;
        private TokenUsage tokenUsage;
        private FinishReason finishReason;

        public T id(String id) {
            this.id = id;
            return (T)this;
        }

        public T modelName(String modelName) {
            this.modelName = modelName;
            return (T)this;
        }

        public T tokenUsage(TokenUsage tokenUsage) {
            this.tokenUsage = tokenUsage;
            return (T)this;
        }

        public T finishReason(FinishReason finishReason) {
            this.finishReason = finishReason;
            return (T)this;
        }

        public ChatResponseMetadata build() {
            return new ChatResponseMetadata(this);
        }
    }
}

