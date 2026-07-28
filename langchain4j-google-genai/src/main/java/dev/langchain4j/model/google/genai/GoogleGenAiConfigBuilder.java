/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.genai.types.Content
 *  com.google.genai.types.FunctionCallingConfig
 *  com.google.genai.types.FunctionCallingConfig$Builder
 *  com.google.genai.types.FunctionDeclaration
 *  com.google.genai.types.GenerateContentConfig
 *  com.google.genai.types.GenerateContentConfig$Builder
 *  com.google.genai.types.GoogleMaps
 *  com.google.genai.types.GoogleSearch
 *  com.google.genai.types.Retrieval
 *  com.google.genai.types.SafetySetting
 *  com.google.genai.types.Schema
 *  com.google.genai.types.ThinkingConfig
 *  com.google.genai.types.ThinkingConfig$Builder
 *  com.google.genai.types.ThinkingLevel
 *  com.google.genai.types.Tool
 *  com.google.genai.types.ToolConfig
 *  com.google.genai.types.UrlContext
 *  com.google.genai.types.VertexAISearch
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 */
package dev.langchain4j.model.google.genai;

import com.google.genai.types.Content;
import com.google.genai.types.FunctionCallingConfig;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GoogleMaps;
import com.google.genai.types.GoogleSearch;
import com.google.genai.types.Retrieval;
import com.google.genai.types.SafetySetting;
import com.google.genai.types.Schema;
import com.google.genai.types.ThinkingConfig;
import com.google.genai.types.ThinkingLevel;
import com.google.genai.types.Tool;
import com.google.genai.types.ToolConfig;
import com.google.genai.types.UrlContext;
import com.google.genai.types.VertexAISearch;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.google.genai.GoogleGenAiToolMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class GoogleGenAiConfigBuilder {
    static GenerateContentConfig buildConfig(ChatRequestParameters parameters, Content systemInstruction, List<SafetySetting> safetySettings, Integer thinkingBudget, String thinkingLevel, Integer seed, boolean googleSearchEnabled, boolean googleMapsEnabled, boolean urlContextEnabled, List<String> allowedFunctionNames, String vertexSearchDatastore, Map<String, String> labels, String cachedContent) {
        return GoogleGenAiConfigBuilder.buildConfig(parameters, systemInstruction, safetySettings, thinkingBudget, thinkingLevel, seed, googleSearchEnabled, googleMapsEnabled, urlContextEnabled, allowedFunctionNames, vertexSearchDatastore, labels, cachedContent, null);
    }

    static GenerateContentConfig buildConfig(ChatRequestParameters parameters, Content systemInstruction, List<SafetySetting> safetySettings, Integer thinkingBudget, String thinkingLevel, Integer seed, boolean googleSearchEnabled, boolean googleMapsEnabled, boolean urlContextEnabled, List<String> allowedFunctionNames, String vertexSearchDatastore, Map<String, String> labels, String cachedContent, Consumer<GenerateContentConfig.Builder> generateContentConfigCustomizer) {
        ResponseFormat responseFormat;
        GenerateContentConfig.Builder configBuilder = GenerateContentConfig.builder();
        if (parameters.temperature() != null) {
            configBuilder.temperature(Float.valueOf(parameters.temperature().floatValue()));
        }
        if (parameters.topP() != null) {
            configBuilder.topP(Float.valueOf(parameters.topP().floatValue()));
        }
        if (parameters.topK() != null) {
            configBuilder.topK(Float.valueOf(parameters.topK().floatValue()));
        }
        if (parameters.frequencyPenalty() != null) {
            configBuilder.frequencyPenalty(Float.valueOf(parameters.frequencyPenalty().floatValue()));
        }
        if (parameters.presencePenalty() != null) {
            configBuilder.presencePenalty(Float.valueOf(parameters.presencePenalty().floatValue()));
        }
        if (parameters.maxOutputTokens() != null) {
            configBuilder.maxOutputTokens(parameters.maxOutputTokens());
        }
        if (parameters.stopSequences() != null) {
            configBuilder.stopSequences(parameters.stopSequences());
        }
        if (!Utils.isNullOrEmpty(safetySettings)) {
            configBuilder.safetySettings(safetySettings);
        }
        if ((responseFormat = parameters.responseFormat()) != null && responseFormat.type() == ResponseFormatType.JSON) {
            configBuilder.responseMimeType("application/json");
            if (responseFormat.jsonSchema() != null) {
                Schema googleSchema = GoogleGenAiToolMapper.convertToGoogleSchema(responseFormat.jsonSchema().rootElement());
                configBuilder.responseSchema(googleSchema);
            }
        }
        if (thinkingBudget != null && thinkingLevel != null) {
            throw new IllegalArgumentException("Cannot use both thinkingBudget and thinkingLevel at the same time");
        }
        if (thinkingBudget != null || thinkingLevel != null) {
            ThinkingConfig.Builder thinkingBuilder = ThinkingConfig.builder();
            if (thinkingBudget != null) {
                thinkingBuilder.thinkingBudget(thinkingBudget);
            }
            if (thinkingLevel != null) {
                thinkingBuilder.thinkingLevel(new ThinkingLevel(thinkingLevel));
            }
            configBuilder.thinkingConfig(thinkingBuilder.build());
        }
        if (seed != null) {
            configBuilder.seed(seed);
        }
        if (systemInstruction != null) {
            configBuilder.systemInstruction(systemInstruction);
        }
        if (labels != null) {
            configBuilder.labels(labels);
        }
        if (cachedContent != null && !cachedContent.trim().isEmpty()) {
            configBuilder.cachedContent(cachedContent);
        }
        GoogleGenAiConfigBuilder.buildTools(configBuilder, parameters, googleSearchEnabled, googleMapsEnabled, urlContextEnabled, allowedFunctionNames, vertexSearchDatastore);
        if (generateContentConfigCustomizer != null) {
            generateContentConfigCustomizer.accept(configBuilder);
        }
        return configBuilder.build();
    }

    private static void buildTools(GenerateContentConfig.Builder configBuilder, ChatRequestParameters parameters, boolean googleSearchEnabled, boolean googleMapsEnabled, boolean urlContextEnabled, List<String> allowedFunctionNames, String vertexSearchDatastore) {
        List toolSpecs = parameters.toolSpecifications();
        ArrayList<Tool> requestTools = new ArrayList<Tool>();
        ArrayList<FunctionDeclaration> functionDeclarations = new ArrayList<FunctionDeclaration>();
        if (toolSpecs != null) {
            for (ToolSpecification toolSpecification : toolSpecs) {
                functionDeclarations.add(GoogleGenAiToolMapper.convertToGoogleFunction(toolSpecification));
            }
        }
        if (!functionDeclarations.isEmpty()) {
            Tool functionTool = Tool.builder().functionDeclarations(functionDeclarations).build();
            requestTools.add(functionTool);
        }
        if (googleSearchEnabled) {
            requestTools.add(Tool.builder().googleSearch(GoogleSearch.builder().build()).build());
        }
        if (googleMapsEnabled) {
            requestTools.add(Tool.builder().googleMaps(GoogleMaps.builder().build()).build());
        }
        if (urlContextEnabled) {
            requestTools.add(Tool.builder().urlContext(UrlContext.builder().build()).build());
        }
        if (vertexSearchDatastore != null && !vertexSearchDatastore.isEmpty()) {
            requestTools.add(Tool.builder().retrieval(Retrieval.builder().vertexAiSearch(VertexAISearch.builder().datastore(vertexSearchDatastore).build()).build()).build());
        }
        if (!requestTools.isEmpty()) {
            configBuilder.tools(requestTools);
        }
        if (!Utils.isNullOrEmpty((Collection)toolSpecs)) {
            FunctionCallingConfig.Builder funcConfig = FunctionCallingConfig.builder();
            ToolChoice toolChoice = parameters.toolChoice();
            if (toolChoice == ToolChoice.REQUIRED) {
                funcConfig.mode("ANY");
            } else if (toolChoice == ToolChoice.NONE) {
                funcConfig.mode("NONE");
            } else {
                funcConfig.mode("AUTO");
            }
            if (!Utils.isNullOrEmpty(allowedFunctionNames)) {
                funcConfig.allowedFunctionNames(allowedFunctionNames);
            }
            configBuilder.toolConfig(ToolConfig.builder().functionCallingConfig(funcConfig.build()).build());
        }
    }

    private GoogleGenAiConfigBuilder() {
    }
}

