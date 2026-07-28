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
public class AnthropicCacheMissReason {
    public String type;
    public Integer cacheMissedInputTokens;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCacheMissedInputTokens() {
        return this.cacheMissedInputTokens;
    }

    public void setCacheMissedInputTokens(Integer cacheMissedInputTokens) {
        this.cacheMissedInputTokens = cacheMissedInputTokens;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnthropicCacheMissReason)) {
            return false;
        }
        AnthropicCacheMissReason that = (AnthropicCacheMissReason)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.cacheMissedInputTokens, that.cacheMissedInputTokens);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.cacheMissedInputTokens);
    }

    public String toString() {
        return "AnthropicCacheMissReason{type='" + this.type + '\'' + ", cacheMissedInputTokens=" + this.cacheMissedInputTokens + '}';
    }
}

