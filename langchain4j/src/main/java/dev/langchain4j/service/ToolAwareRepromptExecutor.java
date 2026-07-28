/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.guardrail.ChatExecutor
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.tool.ToolServiceContext;
import java.util.List;
import java.util.function.Function;

@Internal
final class ToolAwareRepromptExecutor {
    private ToolAwareRepromptExecutor() {
    }

    static ChatExecutor wrap(final ChatExecutor rawChatExecutor, final AiServiceContext context, final Object memoryId, final ChatRequestParameters parameters, final InvocationContext invocationContext, final ToolServiceContext toolServiceContext, final Function<ChatRequest, ChatResponse> chatModelInvoker) {
        return new ChatExecutor(){

            public ChatResponse execute() {
                return rawChatExecutor.execute();
            }

            public ChatResponse execute(List<ChatMessage> chatMessages) {
                ChatResponse initialResponse = rawChatExecutor.execute(chatMessages);
                if (!initialResponse.aiMessage().hasToolExecutionRequests()) {
                    return initialResponse;
                }
                return context.toolService.executeInferenceAndToolsLoop(context, memoryId, initialResponse, parameters, chatMessages, null, invocationContext, toolServiceContext, chatModelInvoker).aggregateResponse();
            }
        };
    }
}

