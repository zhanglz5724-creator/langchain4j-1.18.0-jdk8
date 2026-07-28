/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.openai.internal.audio.texttospeech;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpenAiTextToSpeechRequest {
    @JsonProperty
    private final String input;
    @JsonProperty
    private final String model;
    @JsonProperty
    private final String voice;

    public OpenAiTextToSpeechRequest(Builder builder) {
        this.input = builder.text;
        this.model = builder.model;
        this.voice = builder.voice;
    }

    public String text() {
        return this.input;
    }

    public String model() {
        return this.model;
    }

    public String voice() {
        return this.voice;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String text;
        private String model;
        private String voice;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder voice(String voice) {
            this.voice = voice;
            return this;
        }

        public OpenAiTextToSpeechRequest build() {
            return new OpenAiTextToSpeechRequest(this);
        }
    }
}

