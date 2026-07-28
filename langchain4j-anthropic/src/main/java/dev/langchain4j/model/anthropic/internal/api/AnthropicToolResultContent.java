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
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessageContent;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicToolResultContent
extends AnthropicMessageContent {
    public String toolUseId;
    public Object content;
    public Boolean isError;

    public AnthropicToolResultContent(String toolUseId, String content, Boolean isError) {
        super("tool_result");
        this.toolUseId = toolUseId;
        this.content = content;
        this.isError = isError;
    }

    public AnthropicToolResultContent(String toolUseId, String content, Boolean isError, AnthropicCacheControl cacheControl) {
        super("tool_result", cacheControl);
        this.toolUseId = toolUseId;
        this.content = content;
        this.isError = isError;
    }

    public AnthropicToolResultContent(String toolUseId, List<AnthropicMessageContent> content, Boolean isError) {
        super("tool_result");
        this.toolUseId = toolUseId;
        this.content = content;
        this.isError = isError;
    }

    public AnthropicToolResultContent(String toolUseId, List<AnthropicMessageContent> content, Boolean isError, AnthropicCacheControl cacheControl) {
        super("tool_result", cacheControl);
        this.toolUseId = toolUseId;
        this.content = content;
        this.isError = isError;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AnthropicToolResultContent that = (AnthropicToolResultContent)o;
        return Objects.equals(this.toolUseId, that.toolUseId) && Objects.equals(this.content, that.content) && Objects.equals(this.isError, that.isError);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.toolUseId, this.content, this.isError);
    }

    public String toString() {
        return "AnthropicToolResultContent{isError=" + this.isError + ", type='" + this.type + '\'' + ", cacheControl=" + this.cacheControl + '}';
    }
}

