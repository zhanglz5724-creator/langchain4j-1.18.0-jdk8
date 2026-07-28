/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters$Builder
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import java.util.Objects;

public class AnthropicChatRequestParameters
extends DefaultChatRequestParameters {
    public static final AnthropicChatRequestParameters EMPTY = AnthropicChatRequestParameters.builder().build();
    private final Boolean cacheSystemMessages;
    private final Boolean cacheTools;
    private final String thinkingType;
    private final Integer thinkingBudgetTokens;
    private final Boolean sendThinking;
    private final Boolean midConversationSystemMessages;
    private final Boolean returnThinking;
    private final String toolChoiceName;
    private final Boolean disableParallelToolUse;
    private final String userId;
    private final Boolean returnCacheDiagnostics;
    private final String previousMessageId;

    private AnthropicChatRequestParameters(Builder builder) {
        super((DefaultChatRequestParameters.Builder)builder);
        this.cacheSystemMessages = builder.cacheSystemMessages;
        this.cacheTools = builder.cacheTools;
        this.thinkingType = builder.thinkingType;
        this.thinkingBudgetTokens = builder.thinkingBudgetTokens;
        this.sendThinking = builder.sendThinking;
        this.midConversationSystemMessages = builder.midConversationSystemMessages;
        this.returnThinking = builder.returnThinking;
        this.toolChoiceName = builder.toolChoiceName;
        this.disableParallelToolUse = builder.disableParallelToolUse;
        this.userId = builder.userId;
        this.returnCacheDiagnostics = builder.returnCacheDiagnostics;
        this.previousMessageId = builder.previousMessageId;
    }

    public Boolean cacheSystemMessages() {
        return this.cacheSystemMessages;
    }

    public Boolean cacheTools() {
        return this.cacheTools;
    }

    public String thinkingType() {
        return this.thinkingType;
    }

    public Integer thinkingBudgetTokens() {
        return this.thinkingBudgetTokens;
    }

    public Boolean sendThinking() {
        return this.sendThinking;
    }

    public Boolean midConversationSystemMessages() {
        return this.midConversationSystemMessages;
    }

    public Boolean returnThinking() {
        return this.returnThinking;
    }

    public String toolChoiceName() {
        return this.toolChoiceName;
    }

    public Boolean disableParallelToolUse() {
        return this.disableParallelToolUse;
    }

    public String userId() {
        return this.userId;
    }

    public Boolean returnCacheDiagnostics() {
        return this.returnCacheDiagnostics;
    }

    public String previousMessageId() {
        return this.previousMessageId;
    }

    public AnthropicChatRequestParameters overrideWith(ChatRequestParameters that) {
        return AnthropicChatRequestParameters.builder().overrideWith((ChatRequestParameters)this).overrideWith(that).build();
    }

    public AnthropicChatRequestParameters defaultedBy(ChatRequestParameters that) {
        return AnthropicChatRequestParameters.builder().overrideWith(that).overrideWith((ChatRequestParameters)this).build();
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
        AnthropicChatRequestParameters that = (AnthropicChatRequestParameters)((Object)o);
        return Objects.equals(this.cacheSystemMessages, that.cacheSystemMessages) && Objects.equals(this.cacheTools, that.cacheTools) && Objects.equals(this.thinkingType, that.thinkingType) && Objects.equals(this.thinkingBudgetTokens, that.thinkingBudgetTokens) && Objects.equals(this.sendThinking, that.sendThinking) && Objects.equals(this.midConversationSystemMessages, that.midConversationSystemMessages) && Objects.equals(this.returnThinking, that.returnThinking) && Objects.equals(this.toolChoiceName, that.toolChoiceName) && Objects.equals(this.disableParallelToolUse, that.disableParallelToolUse) && Objects.equals(this.userId, that.userId) && Objects.equals(this.returnCacheDiagnostics, that.returnCacheDiagnostics) && Objects.equals(this.previousMessageId, that.previousMessageId);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), this.cacheSystemMessages, this.cacheTools, this.thinkingType, this.thinkingBudgetTokens, this.sendThinking, this.midConversationSystemMessages, this.returnThinking, this.toolChoiceName, this.disableParallelToolUse, this.userId, this.returnCacheDiagnostics, this.previousMessageId);
    }

    public String toString() {
        return "AnthropicChatRequestParameters{modelName=" + this.modelName() + ", temperature=" + this.temperature() + ", topP=" + this.topP() + ", topK=" + this.topK() + ", frequencyPenalty=" + this.frequencyPenalty() + ", presencePenalty=" + this.presencePenalty() + ", maxOutputTokens=" + this.maxOutputTokens() + ", stopSequences=" + this.stopSequences() + ", toolSpecifications=" + this.toolSpecifications() + ", toolChoice=" + this.toolChoice() + ", responseFormat=" + this.responseFormat() + ", cacheSystemMessages=" + this.cacheSystemMessages + ", cacheTools=" + this.cacheTools + ", thinkingType=" + this.thinkingType + ", thinkingBudgetTokens=" + this.thinkingBudgetTokens + ", sendThinking=" + this.sendThinking + ", midConversationSystemMessages=" + this.midConversationSystemMessages + ", returnThinking=" + this.returnThinking + ", toolChoiceName=" + this.toolChoiceName + ", disableParallelToolUse=" + this.disableParallelToolUse + ", userId=" + this.userId + ", returnCacheDiagnostics=" + this.returnCacheDiagnostics + ", previousMessageId=" + this.previousMessageId + '}';
    }

    public Builder toBuilder() {
        return AnthropicChatRequestParameters.builder().overrideWith((ChatRequestParameters)this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultChatRequestParameters.Builder<Builder> {
        private Boolean cacheSystemMessages;
        private Boolean cacheTools;
        private String thinkingType;
        private Integer thinkingBudgetTokens;
        private Boolean sendThinking;
        private Boolean midConversationSystemMessages;
        private Boolean returnThinking;
        private String toolChoiceName;
        private Boolean disableParallelToolUse;
        private String userId;
        private Boolean returnCacheDiagnostics;
        private String previousMessageId;

        public Builder overrideWith(ChatRequestParameters parameters) {
            super.overrideWith(parameters);
            if (parameters instanceof AnthropicChatRequestParameters) {
                AnthropicChatRequestParameters anthropicParameters = (AnthropicChatRequestParameters)parameters;
                this.cacheSystemMessages((Boolean)Utils.getOrDefault((Object)anthropicParameters.cacheSystemMessages(), (Object)this.cacheSystemMessages));
                this.cacheTools((Boolean)Utils.getOrDefault((Object)anthropicParameters.cacheTools(), (Object)this.cacheTools));
                this.thinkingType((String)Utils.getOrDefault((Object)anthropicParameters.thinkingType(), (Object)this.thinkingType));
                this.thinkingBudgetTokens((Integer)Utils.getOrDefault((Object)anthropicParameters.thinkingBudgetTokens(), (Object)this.thinkingBudgetTokens));
                this.sendThinking((Boolean)Utils.getOrDefault((Object)anthropicParameters.sendThinking(), (Object)this.sendThinking));
                this.midConversationSystemMessages((Boolean)Utils.getOrDefault((Object)anthropicParameters.midConversationSystemMessages(), (Object)this.midConversationSystemMessages));
                this.returnThinking((Boolean)Utils.getOrDefault((Object)anthropicParameters.returnThinking(), (Object)this.returnThinking));
                this.toolChoiceName((String)Utils.getOrDefault((Object)anthropicParameters.toolChoiceName(), (Object)this.toolChoiceName));
                this.disableParallelToolUse((Boolean)Utils.getOrDefault((Object)anthropicParameters.disableParallelToolUse(), (Object)this.disableParallelToolUse));
                this.userId((String)Utils.getOrDefault((Object)anthropicParameters.userId(), (Object)this.userId));
                this.returnCacheDiagnostics((Boolean)Utils.getOrDefault((Object)anthropicParameters.returnCacheDiagnostics(), (Object)this.returnCacheDiagnostics));
                this.previousMessageId((String)Utils.getOrDefault((Object)anthropicParameters.previousMessageId(), (Object)this.previousMessageId));
            }
            return this;
        }

        public Builder modelName(AnthropicChatModelName modelName) {
            return (Builder)super.modelName(modelName == null ? null : modelName.toString());
        }

        public Builder cacheSystemMessages(Boolean cacheSystemMessages) {
            this.cacheSystemMessages = cacheSystemMessages;
            return this;
        }

        public Builder cacheTools(Boolean cacheTools) {
            this.cacheTools = cacheTools;
            return this;
        }

        public Builder thinkingType(String thinkingType) {
            this.thinkingType = thinkingType;
            return this;
        }

        public Builder thinkingBudgetTokens(Integer thinkingBudgetTokens) {
            this.thinkingBudgetTokens = thinkingBudgetTokens;
            return this;
        }

        public Builder sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this;
        }

        public Builder midConversationSystemMessages(Boolean midConversationSystemMessages) {
            this.midConversationSystemMessages = midConversationSystemMessages;
            return this;
        }

        public Builder returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this;
        }

        public Builder toolChoiceName(String toolChoiceName) {
            this.toolChoiceName = toolChoiceName;
            return this;
        }

        public Builder disableParallelToolUse(Boolean disableParallelToolUse) {
            this.disableParallelToolUse = disableParallelToolUse;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder returnCacheDiagnostics(Boolean returnCacheDiagnostics) {
            this.returnCacheDiagnostics = returnCacheDiagnostics;
            return this;
        }

        public Builder previousMessageId(String previousMessageId) {
            this.previousMessageId = previousMessageId;
            return this;
        }

        public AnthropicChatRequestParameters build() {
            return new AnthropicChatRequestParameters(this);
        }
    }
}

