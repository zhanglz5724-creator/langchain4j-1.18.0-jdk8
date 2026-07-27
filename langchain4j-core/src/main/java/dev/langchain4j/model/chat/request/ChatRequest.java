/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChatRequest {
    private final List<ChatMessage> messages;
    private final ChatRequestParameters parameters;

    protected ChatRequest(Builder builder) {
        boolean individualParametersAreSpecified;
        this.messages = Utils.copy(ValidationUtils.ensureNotEmpty(builder.messages, "messages"));
        boolean bl = individualParametersAreSpecified = builder.modelName != null || builder.temperature != null || builder.topP != null || builder.topK != null || builder.frequencyPenalty != null || builder.presencePenalty != null || builder.maxOutputTokens != null || !Utils.isNullOrEmpty(builder.stopSequences) || !Utils.isNullOrEmpty(builder.toolSpecifications) || builder.toolChoice != null || builder.responseFormat != null;
        if (!individualParametersAreSpecified) {
            this.parameters = builder.parameters != null ? builder.parameters : DefaultChatRequestParameters.EMPTY;
            return;
        }
        ChatRequestParameters overrides = ((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)((DefaultChatRequestParameters.Builder)ChatRequestParameters.builder().modelName(builder.modelName)).temperature(builder.temperature)).topP(builder.topP)).topK(builder.topK)).frequencyPenalty(builder.frequencyPenalty)).presencePenalty(builder.presencePenalty)).maxOutputTokens(builder.maxOutputTokens)).stopSequences(builder.stopSequences)).toolSpecifications(builder.toolSpecifications)).toolChoice(builder.toolChoice)).responseFormat(builder.responseFormat)).build();
        this.parameters = builder.parameters != null ? builder.parameters.overrideWith(overrides) : overrides;
    }

    public List<ChatMessage> messages() {
        return this.messages;
    }

    public ChatRequestParameters parameters() {
        return this.parameters;
    }

    public String modelName() {
        return this.parameters.modelName();
    }

    public Double temperature() {
        return this.parameters.temperature();
    }

    public Double topP() {
        return this.parameters.topP();
    }

    public Integer topK() {
        return this.parameters.topK();
    }

    public Double frequencyPenalty() {
        return this.parameters.frequencyPenalty();
    }

    public Double presencePenalty() {
        return this.parameters.presencePenalty();
    }

    public Integer maxOutputTokens() {
        return this.parameters.maxOutputTokens();
    }

    public List<String> stopSequences() {
        return this.parameters.stopSequences();
    }

    public List<ToolSpecification> toolSpecifications() {
        return this.parameters.toolSpecifications();
    }

    public ToolChoice toolChoice() {
        return this.parameters.toolChoice();
    }

    public ResponseFormat responseFormat() {
        return this.parameters.responseFormat();
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChatRequest that = (ChatRequest)o;
        return Objects.equals(this.messages, that.messages) && Objects.equals(this.parameters, that.parameters);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.messages, this.parameters);
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ChatRequest { messages = " + this.messages + ", parameters = " + this.parameters + " }";
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<ChatMessage> messages;
        private ChatRequestParameters parameters;
        private String modelName;
        private Double temperature;
        private Double topP;
        private Integer topK;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private Integer maxOutputTokens;
        private List<String> stopSequences;
        private List<ToolSpecification> toolSpecifications;
        private ToolChoice toolChoice;
        private ResponseFormat responseFormat;

        public Builder() {
        }

        public Builder(ChatRequest chatRequest) {
            this.messages = chatRequest.messages;
            this.parameters = chatRequest.parameters;
        }

        public Builder messages(List<ChatMessage> messages) {
            this.messages = messages;
            return this;
        }

        public Builder messages(ChatMessage ... messages) {
            return this.messages(Arrays.asList(messages));
        }

        public Builder parameters(ChatRequestParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        public Builder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public Builder toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return this;
        }

        public Builder toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public Builder toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
            return this;
        }

        public Builder responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public ChatRequest build() {
            return new ChatRequest(this);
        }
    }
}

