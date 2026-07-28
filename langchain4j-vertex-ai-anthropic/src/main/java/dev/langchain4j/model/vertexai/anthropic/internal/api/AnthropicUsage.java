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
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicUsage {
    public Integer inputTokens;
    public Integer outputTokens;
    public Integer cacheCreationInputTokens;
    public Integer cacheReadInputTokens;

    public AnthropicUsage() {
    }

    public AnthropicUsage(Integer inputTokens, Integer outputTokens) {
        this.inputTokens = inputTokens;
        this.outputTokens = outputTokens;
    }

    public Integer getInputTokens() {
        return this.inputTokens;
    }

    public Integer getOutputTokens() {
        return this.outputTokens;
    }

    public Integer getCacheCreationInputTokens() {
        return this.cacheCreationInputTokens;
    }

    public Integer getCacheReadInputTokens() {
        return this.cacheReadInputTokens;
    }

    public String toString() {
        return new StringJoiner(", ", "AnthropicUsage [", "]").add("inputTokens" + this.getInputTokens()).add("outputTokens" + this.getOutputTokens()).add("cacheCreationInputTokens" + this.getCacheCreationInputTokens()).add("cacheReadInputTokens" + this.getCacheReadInputTokens()).toString();
    }
}

