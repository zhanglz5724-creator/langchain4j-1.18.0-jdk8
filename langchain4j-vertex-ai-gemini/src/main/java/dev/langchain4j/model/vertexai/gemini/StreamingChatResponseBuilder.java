package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.*;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

class StreamingChatResponseBuilder {

    private final StringBuffer contentBuilder = new StringBuffer();

    private final List<FunctionCall> functionCalls = new ArrayList<>();

    private volatile TokenUsage tokenUsage;
    private volatile FinishReason finishReason;
     class TextAndFunctions {
        private final String text;
        private final List<FunctionCall> functionCalls;

        public TextAndFunctions(String text, List<FunctionCall> functionCalls) {
            this.text = text;
            this.functionCalls = functionCalls;
        }

        public String getText() {
            return text;
        }

        public List<FunctionCall> getFunctionCalls() {
            return functionCalls;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TextAndFunctions that = (TextAndFunctions) o;
            return java.util.Objects.equals(this.text, that.text) && java.util.Objects.equals(this.functionCalls, that.functionCalls);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(text, functionCalls);
        }

        @Override
        public String toString() {
            return "TextAndFunctions{"text=" + text + , "functionCalls=" + functionCalls + "}"";
        }

    }

    TextAndFunctions append(GenerateContentResponse partialResponse) {
        if (partialResponse == null) {
            return new TextAndFunctions(null, Collections.emptyList());
        }

        List<Candidate> candidates = partialResponse.getCandidatesList();
        if (candidates.isEmpty() || candidates.get(0) == null) {
            return new TextAndFunctions(null, Collections.emptyList());
        }

        String text = ResponseHandler.getText(partialResponse);
        contentBuilder.append(text);

        List<FunctionCall> functionCalls = candidates.stream()
                .map(Candidate::getContent)
                .map(Content::getPartsList)
                .flatMap(List::stream)
                .filter(Part::hasFunctionCall)
                .map(Part::getFunctionCall)
                .collect(Collectors.toList());

        if (!functionCalls.isEmpty()) {
            this.functionCalls.addAll(functionCalls);
        }

        if (partialResponse.hasUsageMetadata()) {
            tokenUsage = TokenUsageMapper.map(partialResponse.getUsageMetadata());
        }

        Candidate.FinishReason finishReason = ResponseHandler.getFinishReason(partialResponse);
        if (finishReason != null) {
            this.finishReason = FinishReasonMapper.map(finishReason);
        }

        return new TextAndFunctions(text, functionCalls);
    }

    Response<AiMessage> build() {
        if (!functionCalls.isEmpty()) {
            return Response.from(
                AiMessage.from(FunctionCallHelper.fromFunctionCalls(functionCalls)),
                tokenUsage,
                finishReason
            );
        } else {
            return Response.from(
                AiMessage.from(contentBuilder.toString()),
                tokenUsage,
                finishReason
            );
        }
    }
}
