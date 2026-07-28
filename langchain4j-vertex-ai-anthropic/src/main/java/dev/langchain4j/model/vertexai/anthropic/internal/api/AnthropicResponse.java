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
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicContent;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicUsage;
import java.util.List;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicResponse {
    public String id;
    public String type;
    public String role;
    public String model;
    public List<AnthropicContent> content;
    public String stopReason;
    public String stopSequence;
    public AnthropicUsage usage;

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getRole() {
        return this.role;
    }

    public String getModel() {
        return this.model;
    }

    public List<AnthropicContent> getContent() {
        return this.content;
    }

    public String getStopReason() {
        return this.stopReason;
    }

    public String getStopSequence() {
        return this.stopSequence;
    }

    public AnthropicUsage getUsage() {
        return this.usage;
    }

    public String toString() {
        return new StringJoiner(", ", "AnthropicResponse [", "]").add("id=" + this.getId()).add("type=" + this.getType()).add("role=" + this.getRole()).add("model=" + this.getModel()).add("content=" + (this.getContent() == null ? 0 : this.getContent().size())).add("stopReason=" + this.getStopReason()).add("stopSequence=" + this.getStopSequence()).add("usage=" + this.getUsage()).toString();
    }
}

