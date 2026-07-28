/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 */
package dev.langchain4j.model.mistralai;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.mistralai.internal.api.MistralAiChatCompletionRequest;
import dev.langchain4j.model.mistralai.internal.mapper.MistralAiMapper;
import java.util.ArrayList;
import java.util.Collection;

@Internal
class InternalMistralAIHelper {
    private InternalMistralAIHelper() {
    }

    static void validate(ChatRequestParameters parameters) {
        ArrayList<String> unsupportedFeatures = new ArrayList<String>();
        if (parameters.topK() != null) {
            unsupportedFeatures.add("topK");
        }
        if (!unsupportedFeatures.isEmpty()) {
            if (unsupportedFeatures.size() == 1) {
                throw new UnsupportedFeatureException((String)unsupportedFeatures.get(0) + " is not supported by Mistral AI");
            }
            throw new UnsupportedFeatureException(String.join((CharSequence)", ", unsupportedFeatures) + " are not supported by Mistral AI");
        }
    }

    static MistralAiChatCompletionRequest createMistralAiRequest(ChatRequest chatRequest, Boolean safePrompt, Integer randomSeed, boolean stream, boolean sendThinking, boolean strictJsonSchema) {
        MistralAiChatCompletionRequest.MistralAiChatCompletionRequestBuilder requestBuilder = MistralAiChatCompletionRequest.builder().model(chatRequest.modelName()).messages(MistralAiMapper.toMistralAiMessages(chatRequest.messages(), sendThinking)).temperature(chatRequest.temperature()).maxTokens(chatRequest.maxOutputTokens()).topP(chatRequest.topP()).randomSeed(randomSeed).safePrompt(safePrompt).responseFormat(MistralAiMapper.toMistralAiResponseFormat(chatRequest.responseFormat(), strictJsonSchema)).stop(chatRequest.stopSequences().toArray(new String[0])).frequencyPenalty(chatRequest.frequencyPenalty()).presencePenalty(chatRequest.presencePenalty()).stream(stream);
        if (!Utils.isNullOrEmpty((Collection)chatRequest.toolSpecifications())) {
            requestBuilder.tools(MistralAiMapper.toMistralAiTools(chatRequest.toolSpecifications()));
        }
        if (chatRequest.toolChoice() != null) {
            requestBuilder.toolChoice(MistralAiMapper.toMistralAiToolChoiceName(chatRequest.toolChoice()));
        }
        return requestBuilder.build();
    }
}

