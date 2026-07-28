/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiFunctionDeclaration;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.Json;
import dev.langchain4j.model.googleai.SchemaMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class FunctionMapper {
    FunctionMapper() {
    }

    static List<GeminiGenerateContentRequest.GeminiTool> fromToolSpecsToGTools(List<ToolSpecification> specifications, boolean allowCodeExecution, boolean allowGoogleSearch, boolean allowUrlContext, boolean allowGoogleMaps, boolean retrieveGoogleMapsWidgetToken) {
        if (Utils.isNullOrEmpty(specifications)) {
            if (allowCodeExecution || allowGoogleSearch || allowUrlContext || allowGoogleMaps) {
                return Collections.singletonList(new GeminiGenerateContentRequest.GeminiTool(null, allowCodeExecution ? new GeminiGenerateContentRequest.GeminiTool.GeminiCodeExecution() : null, allowGoogleSearch ? new GeminiGenerateContentRequest.GeminiTool.GeminiGoogleSearchRetrieval() : null, allowUrlContext ? new GeminiGenerateContentRequest.GeminiTool.GeminiUrlContext() : null, allowGoogleMaps ? new GeminiGenerateContentRequest.GeminiTool.GeminiGoogleMaps(retrieveGoogleMapsWidgetToken) : null));
            }
            return null;
        }
        List<GeminiFunctionDeclaration> functionDeclarations = specifications.stream().map(specification -> {
            GeminiFunctionDeclaration.Builder fnBuilder = GeminiFunctionDeclaration.builder().name(specification.name());
            if (specification.description() != null) {
                fnBuilder.description(specification.description());
            }
            if (specification.parameters() != null) {
                fnBuilder.parameters(SchemaMapper.fromJsonSchemaToGSchema((JsonSchemaElement)specification.parameters()));
            }
            return fnBuilder.build();
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return Collections.singletonList(new GeminiGenerateContentRequest.GeminiTool(functionDeclarations.isEmpty() ? null : functionDeclarations, allowCodeExecution ? new GeminiGenerateContentRequest.GeminiTool.GeminiCodeExecution() : null, allowGoogleSearch ? new GeminiGenerateContentRequest.GeminiTool.GeminiGoogleSearchRetrieval() : null, allowUrlContext ? new GeminiGenerateContentRequest.GeminiTool.GeminiUrlContext() : null, allowGoogleMaps ? new GeminiGenerateContentRequest.GeminiTool.GeminiGoogleMaps(retrieveGoogleMapsWidgetToken) : null));
    }

    static List<ToolExecutionRequest> toToolExecutionRequests(List<GeminiContent.GeminiPart.GeminiFunctionCall> functionCalls) {
        return functionCalls.stream().map(functionCall -> ToolExecutionRequest.builder().id(functionCall.id()).name(functionCall.name()).arguments(Json.toJsonWithoutIndent(functionCall.args())).build()).collect(Collectors.toList());
    }
}

