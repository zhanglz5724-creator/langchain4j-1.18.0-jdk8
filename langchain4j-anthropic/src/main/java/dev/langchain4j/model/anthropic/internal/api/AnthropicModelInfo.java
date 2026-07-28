/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicModelInfo {
    public String id;
    public String createdAt;
    public String displayName;
    public String type;
    public Integer maxInputTokens;
    @JsonProperty(value="max_tokens")
    public Integer maxOutputTokens;

    public int hashCode() {
        return Objects.hash(this.id, this.createdAt, this.displayName, this.type, this.maxInputTokens, this.maxOutputTokens);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnthropicModelInfo)) {
            return false;
        }
        AnthropicModelInfo that = (AnthropicModelInfo)obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.type, that.type) && Objects.equals(this.maxInputTokens, that.maxInputTokens) && Objects.equals(this.maxOutputTokens, that.maxOutputTokens);
    }

    public String toString() {
        return "AnthropicModelInfo{id='" + this.id + '\'' + ", createdAt='" + this.createdAt + '\'' + ", displayName='" + this.displayName + '\'' + ", type='" + this.type + '\'' + ", maxInputTokens=" + this.maxInputTokens + ", maxOutputTokens=" + this.maxOutputTokens + '}';
    }
}

