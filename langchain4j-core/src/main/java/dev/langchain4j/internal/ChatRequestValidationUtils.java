/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import java.util.List;
import java.util.Locale;

@Internal
public class ChatRequestValidationUtils {
    public static void validateMessages(List<ChatMessage> messages) {
        for (ChatMessage message : messages) {
            if (!(message instanceof UserMessage)) continue;
            UserMessage userMessage = (UserMessage)message;
            for (Content content : userMessage.contents()) {
                if (content.type() == ContentType.TEXT) continue;
                throw new UnsupportedFeatureException(String.format("Content of type %s is not supported yet by this model provider", content.type().toString().toLowerCase(Locale.ROOT)));
            }
        }
    }

    public static void validateParameters(ChatRequestParameters parameters) {
        String errorTemplate = "%s is not supported yet by this model provider";
        if (parameters.modelName() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'modelName' parameter"));
        }
        if (parameters.temperature() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'temperature' parameter"));
        }
        if (parameters.topP() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'topP' parameter"));
        }
        if (parameters.topK() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'topK' parameter"));
        }
        if (parameters.frequencyPenalty() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'frequencyPenalty' parameter"));
        }
        if (parameters.presencePenalty() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'presencePenalty' parameter"));
        }
        if (parameters.maxOutputTokens() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'maxOutputTokens' parameter"));
        }
        if (!parameters.stopSequences().isEmpty()) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'stopSequences' parameter"));
        }
    }

    public static void validate(List<ToolSpecification> toolSpecifications) {
        if (!Utils.isNullOrEmpty(toolSpecifications)) {
            throw new UnsupportedFeatureException("tools are not supported yet by this model provider");
        }
    }

    public static void validate(ToolChoice toolChoice) {
        if (toolChoice != null && toolChoice != ToolChoice.AUTO) {
            throw new UnsupportedFeatureException(String.format("%s.%s is not supported yet by this model provider", new Object[]{ToolChoice.class.getSimpleName(), toolChoice}));
        }
    }

    public static void validate(ResponseFormat responseFormat) {
        String errorTemplate = "%s is not supported yet by this model provider";
        if (responseFormat != null && responseFormat.type() == ResponseFormatType.JSON) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "JSON response format"));
        }
    }
}

