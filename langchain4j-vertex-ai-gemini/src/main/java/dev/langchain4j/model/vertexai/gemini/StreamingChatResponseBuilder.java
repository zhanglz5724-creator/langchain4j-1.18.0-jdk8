/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.Candidate
 *  com.google.cloud.vertexai.api.Candidate$FinishReason
 *  com.google.cloud.vertexai.api.Content
 *  com.google.cloud.vertexai.api.FunctionCall
 *  com.google.cloud.vertexai.api.GenerateContentResponse
 *  com.google.cloud.vertexai.api.Part
 *  com.google.cloud.vertexai.generativeai.ResponseHandler
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.Candidate;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.FunctionCall;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.gemini.FinishReasonMapper;
import dev.langchain4j.model.vertexai.gemini.FunctionCallHelper;
import dev.langchain4j.model.vertexai.gemini.TokenUsageMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class StreamingChatResponseBuilder {
    private final StringBuffer contentBuilder = new StringBuffer();
    private final List<FunctionCall> functionCalls = new ArrayList<FunctionCall>();
    private volatile TokenUsage tokenUsage;
    private volatile FinishReason finishReason;

    StreamingChatResponseBuilder() {
    }

    TextAndFunctions append(GenerateContentResponse partialResponse) {
        Candidate.FinishReason finishReason;
        if (partialResponse == null) {
            return new TextAndFunctions(null, Collections.emptyList());
        }
        List candidates = partialResponse.getCandidatesList();
        if (candidates.isEmpty() || candidates.get(0) == null) {
            return new TextAndFunctions(null, Collections.emptyList());
        }
        String text = ResponseHandler.getText((GenerateContentResponse)partialResponse);
        this.contentBuilder.append(text);
        List<FunctionCall> functionCalls = candidates.stream().map(Candidate::getContent).map(Content::getPartsList).flatMap(Collection::stream).filter(Part::hasFunctionCall).map(Part::getFunctionCall).collect(Collectors.toList());
        if (!functionCalls.isEmpty()) {
            this.functionCalls.addAll(functionCalls);
        }
        if (partialResponse.hasUsageMetadata()) {
            this.tokenUsage = TokenUsageMapper.map(partialResponse.getUsageMetadata());
        }
        if ((finishReason = ResponseHandler.getFinishReason((GenerateContentResponse)partialResponse)) != null) {
            this.finishReason = FinishReasonMapper.map(finishReason);
        }
        return new TextAndFunctions(text, functionCalls);
    }

    Response<AiMessage> build() {
        if (!this.functionCalls.isEmpty()) {
            return Response.from(AiMessage.from(FunctionCallHelper.fromFunctionCalls(this.functionCalls)), (TokenUsage)this.tokenUsage, (FinishReason)this.finishReason);
        }
        return Response.from(AiMessage.from((String)this.contentBuilder.toString()), (TokenUsage)this.tokenUsage, (FinishReason)this.finishReason);
    }

    static final class TextAndFunctions {
        final String text;
        final List<FunctionCall> functionCalls;

        TextAndFunctions(String text, List<FunctionCall> functionCalls) {
            this.text = text;
            this.functionCalls = functionCalls;
        }
    }
}

