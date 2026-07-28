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
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicUsage {
    public Integer inputTokens;
    public Integer outputTokens;
    public Integer cacheCreationInputTokens;
    public Integer cacheReadInputTokens;

    public int hashCode() {
        return Objects.hash(this.inputTokens, this.outputTokens, this.cacheCreationInputTokens, this.cacheReadInputTokens);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnthropicUsage)) {
            return false;
        }
        AnthropicUsage that = (AnthropicUsage)obj;
        return Objects.equals(this.inputTokens, that.inputTokens) && Objects.equals(this.outputTokens, that.outputTokens) && Objects.equals(this.cacheCreationInputTokens, that.cacheCreationInputTokens) && Objects.equals(this.cacheReadInputTokens, that.cacheReadInputTokens);
    }

    public String toString() {
        return "AnthropicUsage{inputTokens=" + this.inputTokens + ", outputTokens=" + this.outputTokens + ", cacheCreationInputTokens=" + this.cacheCreationInputTokens + ", cacheReadInputTokens=" + this.cacheReadInputTokens + '}';
    }
}

