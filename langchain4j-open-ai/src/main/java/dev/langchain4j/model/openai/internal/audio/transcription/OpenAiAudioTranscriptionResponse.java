/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.audio.transcription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.audio.transcription.AudioTokenUsage;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import java.util.Objects;

@JsonDeserialize(builder = OpenAiAudioTranscriptionResponse.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class OpenAiAudioTranscriptionResponse {
    @JsonProperty
    private final String text;
    @JsonProperty
    private final AudioTokenUsage usage;

    public OpenAiAudioTranscriptionResponse(Builder builder) {
        this.text = builder.text;
        this.usage = builder.usage;
    }

    public String text() {
        return this.text;
    }

    public AudioTokenUsage usage() {
        return this.usage;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof OpenAiAudioTranscriptionResponse && this.equalTo((OpenAiAudioTranscriptionResponse)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(OpenAiAudioTranscriptionResponse another) {
        return Objects.equals(this.text, another.text) && Objects.equals(this.usage, another.usage);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.text);
        h += (h << 5) + Objects.hashCode(this.usage);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "OpenAiAudioTranscriptionResponse{text=" + this.text + ", usage=" + this.usage + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String text;
        private AudioTokenUsage usage;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder usage(AudioTokenUsage usage) {
            this.usage = usage;
            return this;
        }

        public OpenAiAudioTranscriptionResponse build() {
            return new OpenAiAudioTranscriptionResponse(this);
        }
    }
}

