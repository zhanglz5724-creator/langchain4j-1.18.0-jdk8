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
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheControl;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class AnthropicMessageContent {
    public String type;
    public AnthropicCacheControl cacheControl;

    public AnthropicMessageContent(String type) {
        this.type = type;
    }

    public AnthropicMessageContent(String type, AnthropicCacheControl cacheControl) {
        this.type = type;
        this.cacheControl = cacheControl;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicMessageContent that = (AnthropicMessageContent)o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.cacheControl, that.cacheControl);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.cacheControl);
    }
}

