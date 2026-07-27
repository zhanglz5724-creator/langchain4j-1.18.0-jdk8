/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.moderation;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModelListenerUtils;
import dev.langchain4j.model.moderation.ModerationRequest;
import dev.langchain4j.model.moderation.ModerationResponse;
import dev.langchain4j.model.moderation.listener.ModerationModelListener;
import dev.langchain4j.model.output.Response;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public interface ModerationModel {
    default public ModerationResponse moderate(ModerationRequest moderationRequest) {
        ModerationRequest finalRequest = moderationRequest.toBuilder().modelName(Utils.getOrDefault(moderationRequest.modelName(), this.modelName())).build();
        ModelProvider modelProvider = this.provider();
        List<ModerationModelListener> listeners = this.listeners();
        ConcurrentHashMap<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();
        ModerationModelListenerUtils.onRequest(finalRequest, modelProvider, attributes, listeners);
        try {
            ModerationResponse moderationResponse = this.doModerate(finalRequest);
            ModerationModelListenerUtils.onResponse(moderationResponse, finalRequest, modelProvider, attributes, listeners);
            return moderationResponse;
        }
        catch (Exception error) {
            ModerationModelListenerUtils.onError(error, finalRequest, modelProvider, attributes, listeners);
            throw error;
        }
    }

    default public ModerationResponse doModerate(ModerationRequest moderationRequest) {
        throw new RuntimeException("Not implemented");
    }

    default public Response<Moderation> moderate(String text) {
        ModerationRequest request = ModerationRequest.builder().texts(Collections.singletonList(text)).build();
        ModerationResponse response = this.moderate(request);
        return Response.from(response.moderation(), null, null, response.metadata());
    }

    default public Response<Moderation> moderate(Prompt prompt) {
        return this.moderate(prompt.text());
    }

    default public Response<Moderation> moderate(ChatMessage message) {
        return this.moderate(Collections.singletonList(message));
    }

    default public Response<Moderation> moderate(List<ChatMessage> messages) {
        List<String> texts = messages.stream().map(ModerationModel::toText).collect(Collectors.toList());
        ModerationRequest request = ModerationRequest.builder().texts(texts).build();
        ModerationResponse response = this.moderate(request);
        return Response.from(response.moderation(), null, null, response.metadata());
    }

    default public Response<Moderation> moderate(TextSegment textSegment) {
        return this.moderate(textSegment.text());
    }

    public static String toText(ChatMessage chatMessage) {
        if (chatMessage instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)chatMessage;
            return systemMessage.text();
        }
        if (chatMessage instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)chatMessage;
            return userMessage.singleText();
        }
        if (chatMessage instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)chatMessage;
            return aiMessage.text();
        }
        if (chatMessage instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)chatMessage;
            return toolExecutionResultMessage.text();
        }
        throw new IllegalArgumentException("Unsupported message type: " + (Object)((Object)chatMessage.type()));
    }

    default public List<ModerationModelListener> listeners() {
        return Collections.emptyList();
    }

    default public ModelProvider provider() {
        return ModelProvider.OTHER;
    }

    default public String modelName() {
        return "unknown";
    }
}

