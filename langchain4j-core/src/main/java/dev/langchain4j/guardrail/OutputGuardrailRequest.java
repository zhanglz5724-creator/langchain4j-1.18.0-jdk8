/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailRequestParams;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.List;
import java.util.Optional;

public final class OutputGuardrailRequest
implements GuardrailRequest<OutputGuardrailRequest> {
    private final ChatResponse responseFromLLM;
    private final ChatExecutor chatExecutor;
    private final GuardrailRequestParams requestParams;

    private OutputGuardrailRequest(Builder builder) {
        this.responseFromLLM = ValidationUtils.ensureNotNull(builder.responseFromLLM, "responseFromLLM");
        this.requestParams = ValidationUtils.ensureNotNull(builder.requestParams, "requestParams");
        this.chatExecutor = ValidationUtils.ensureNotNull(builder.chatExecutor, "chatExecutor");
    }

    public ChatResponse responseFromLLM() {
        return this.responseFromLLM;
    }

    public ChatExecutor chatExecutor() {
        return this.chatExecutor;
    }

    @Override
    public GuardrailRequestParams requestParams() {
        return this.requestParams;
    }

    @Override
    public OutputGuardrailRequest withText(String text) {
        ValidationUtils.ensureNotNull(text, "text");
        AiMessage aiMessage = Optional.ofNullable(this.responseFromLLM.aiMessage().toolExecutionRequests()).filter(t -> !t.isEmpty()).map(t -> new AiMessage(text, (List<ToolExecutionRequest>)t)).orElseGet(() -> new AiMessage(text));
        ChatResponse chatResponse = ChatResponse.builder().aiMessage(aiMessage).metadata(this.responseFromLLM.metadata()).build();
        return OutputGuardrailRequest.builder().responseFromLLM(chatResponse).chatExecutor(this.chatExecutor).requestParams(this.requestParams).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ChatResponse responseFromLLM;
        private ChatExecutor chatExecutor;
        private GuardrailRequestParams requestParams;

        private Builder() {
        }

        public Builder responseFromLLM(ChatResponse responseFromLLM) {
            this.responseFromLLM = responseFromLLM;
            return this;
        }

        public Builder chatExecutor(ChatExecutor chatExecutor) {
            this.chatExecutor = chatExecutor;
            return this;
        }

        public Builder requestParams(GuardrailRequestParams requestParams) {
            this.requestParams = requestParams;
            return this;
        }

        public OutputGuardrailRequest build() {
            return new OutputGuardrailRequest(this);
        }
    }
}

