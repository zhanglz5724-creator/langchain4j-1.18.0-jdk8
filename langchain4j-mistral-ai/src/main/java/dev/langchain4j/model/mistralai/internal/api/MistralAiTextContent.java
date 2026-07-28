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
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MistralAiTextContent
extends MistralAiMessageContent {
    public String text;

    @JsonCreator
    public MistralAiTextContent(@JsonProperty(value="text") String text) {
        super("text");
        this.text = text;
    }

    @Override
    public String asText() {
        return this.text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MistralAiTextContent that = (MistralAiTextContent)o;
        return Objects.equals(this.text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.text);
    }

    public String toString() {
        return "MistralAiTextContent{text='" + this.text + '\'' + ", type='" + this.type + '\'' + '}';
    }
}

