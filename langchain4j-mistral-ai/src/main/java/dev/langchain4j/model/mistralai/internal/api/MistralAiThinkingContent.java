/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTextContent;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MistralAiThinkingContent
extends MistralAiMessageContent {
    private final List<MistralAiTextContent> thinking;

    @JsonCreator
    public MistralAiThinkingContent(@JsonProperty(value="thinking") List<MistralAiTextContent> thinking) {
        super("thinking");
        this.thinking = thinking;
    }

    public List<MistralAiTextContent> getThinking() {
        return this.thinking;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MistralAiThinkingContent that = (MistralAiThinkingContent)o;
        return Objects.equals(this.thinking, that.thinking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.thinking);
    }

    public String toString() {
        return "MistralAiThinkingContent{thinking=" + this.thinking + ", type='" + this.type + '\'' + '}';
    }
}

