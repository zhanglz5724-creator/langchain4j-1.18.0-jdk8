/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailRequestParams;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InputGuardrailRequest
implements GuardrailRequest<InputGuardrailRequest> {
    private final UserMessage userMessage;
    private final GuardrailRequestParams commonParams;

    private InputGuardrailRequest(Builder builder) {
        this.userMessage = ValidationUtils.ensureNotNull(builder.userMessage, "userMessage");
        this.commonParams = ValidationUtils.ensureNotNull(builder.commonParams, "requestParams");
    }

    public UserMessage userMessage() {
        return this.userMessage;
    }

    @Override
    public GuardrailRequestParams requestParams() {
        return this.commonParams;
    }

    @Override
    public InputGuardrailRequest withText(String text) {
        return new Builder().userMessage(this.rewriteUserMessage(text)).commonParams(this.commonParams).build();
    }

    public UserMessage rewriteUserMessage(String text) {
        if (Objects.isNull(this.userMessage) || Objects.isNull(text)) {
            return this.userMessage;
        }
        List<Content> rewrittenContent = this.userMessage.contents().stream().map(c -> c.type() == ContentType.TEXT ? new TextContent(text) : c).collect(Collectors.toList());
        return this.userMessage.toBuilder().contents(rewrittenContent).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UserMessage userMessage;
        private GuardrailRequestParams commonParams;

        public Builder userMessage(UserMessage userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public Builder commonParams(GuardrailRequestParams commonParams) {
            this.commonParams = commonParams;
            return this;
        }

        public InputGuardrailRequest build() {
            return new InputGuardrailRequest(this);
        }
    }
}

