/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.invocation.InvocationParameters
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import java.util.Collections;
import java.util.List;

public class ToolProviderRequest {
    private final InvocationContext invocationContext;
    private final UserMessage userMessage;
    private final List<ChatMessage> messages;

    public ToolProviderRequest(Builder builder) {
        this.invocationContext = (InvocationContext)ValidationUtils.ensureNotNull((Object)builder.invocationContext, (String)"invocationContext");
        this.userMessage = (UserMessage)ValidationUtils.ensureNotNull((Object)builder.userMessage, (String)"userMessage");
        this.messages = Utils.copy((List)builder.messages);
    }

    public ToolProviderRequest(Object chatMemoryId, UserMessage userMessage) {
        this.invocationContext = InvocationContext.builder().chatMemoryId(chatMemoryId).build();
        this.userMessage = (UserMessage)ValidationUtils.ensureNotNull((Object)userMessage, (String)"userMessage");
        this.messages = Collections.emptyList();
    }

    public InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public InvocationParameters invocationParameters() {
        return this.invocationContext.invocationParameters();
    }

    public UserMessage userMessage() {
        return this.userMessage;
    }

    public Object chatMemoryId() {
        return this.invocationContext.chatMemoryId();
    }

    public List<ChatMessage> messages() {
        return this.messages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InvocationContext invocationContext;
        private UserMessage userMessage;
        private List<ChatMessage> messages;

        public Builder invocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
            return this;
        }

        public Builder userMessage(UserMessage userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public Builder messages(List<ChatMessage> messages) {
            this.messages = messages;
            return this;
        }

        public ToolProviderRequest build() {
            return new ToolProviderRequest(this);
        }
    }
}

