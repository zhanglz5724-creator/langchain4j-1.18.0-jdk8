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
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicCacheControl;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicSystemMessage {
    public String type;
    public String text;
    public AnthropicCacheControl cacheControl;

    public AnthropicSystemMessage() {
    }

    public AnthropicSystemMessage(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public static AnthropicSystemMessage textSystemMessage(String text) {
        return new AnthropicSystemMessage("text", text);
    }

    public static AnthropicSystemMessage textSystemMessageWithCache(String text, AnthropicCacheControl cacheControl) {
        AnthropicSystemMessage message = new AnthropicSystemMessage("text", text);
        message.cacheControl = cacheControl;
        return message;
    }
}

