/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.vertexai.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicMessage;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicSystemMessage;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicTool;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicToolChoice;
import java.util.List;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicRequest {
    public String anthropicVersion;
    public List<AnthropicMessage> messages;
    public List<AnthropicSystemMessage> system;
    public Integer maxTokens;
    public List<String> stopSequences;
    public Boolean stream;
    public Double temperature;
    public Double topP;
    public Integer topK;
    public List<AnthropicTool> tools;
    public AnthropicToolChoice toolChoice;

    public AnthropicRequest() {
    }

    public AnthropicRequest(List<AnthropicMessage> messages, List<AnthropicSystemMessage> system, Integer maxTokens, List<String> stopSequences, Boolean stream, Double temperature, Double topP, Integer topK, List<AnthropicTool> tools, AnthropicToolChoice toolChoice) {
        this.messages = messages;
        this.system = system;
        this.maxTokens = maxTokens;
        this.stopSequences = stopSequences;
        this.stream = stream;
        this.temperature = temperature;
        this.topP = topP;
        this.topK = topK;
        this.tools = tools;
        this.toolChoice = toolChoice;
        this.anthropicVersion = "vertex-2023-10-16";
    }

    public String getAnthropicVersion() {
        return this.anthropicVersion;
    }

    public List<AnthropicMessage> getMessages() {
        return this.messages;
    }

    public Integer getMaxTokens() {
        return this.maxTokens;
    }

    public Boolean getStream() {
        return this.stream;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public Double getTopP() {
        return this.topP;
    }

    public Integer getTopK() {
        return this.topK;
    }

    public List<AnthropicTool> getTools() {
        return this.tools;
    }

    public AnthropicToolChoice getToolChoice() {
        return this.toolChoice;
    }

    public String toString() {
        return new StringJoiner(", ", "AnthropicRequest [", "]").add("anthropicVersion=" + this.getAnthropicVersion()).add("messages=" + (this.getMessages() == null ? 0 : this.getMessages().size())).add("maxTokens=" + this.getMaxTokens()).add("stream=" + this.getStream()).add("temperature=" + this.getTemperature()).add("topP=" + this.getTopP()).add("topK=" + this.getTopK()).add("tools=" + this.getTools()).add("toolsChoice=" + this.getToolChoice()).toString();
    }
}

