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
public class AnthropicToolChoice {
    public String type;
    public String name;

    public AnthropicToolChoice() {
    }

    public AnthropicToolChoice(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return new StringJoiner(", ", "AnthropicToolChoice [", "]").add("name=" + this.getName()).add("type=" + this.getType()).toString();
    }

    public static AnthropicToolChoice auto() {
        return new AnthropicToolChoice("auto", null);
    }

    public static AnthropicToolChoice any() {
        return new AnthropicToolChoice("any", null);
    }

    public static AnthropicToolChoice tool(String name) {
        return new AnthropicToolChoice("tool", name);
    }
}

