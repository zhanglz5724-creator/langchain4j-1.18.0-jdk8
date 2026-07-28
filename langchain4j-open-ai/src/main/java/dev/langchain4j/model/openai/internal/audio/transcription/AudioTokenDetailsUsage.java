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
import java.util.Objects;

@JsonDeserialize(builder=AudioTokenDetailsUsage.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AudioTokenDetailsUsage {
    @JsonProperty
    private final Integer textTokens;
    @JsonProperty
    private final Integer audioTokens;

    public AudioTokenDetailsUsage(Builder builder) {
        this.textTokens = builder.textTokens;
        this.audioTokens = builder.audioTokens;
    }

    public Integer textTokens() {
        return this.textTokens;
    }

    public Integer audioTokens() {
        return this.audioTokens;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof AudioTokenDetailsUsage && this.equalTo((AudioTokenDetailsUsage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(AudioTokenDetailsUsage another) {
        return Objects.equals(this.textTokens, another.textTokens) && Objects.equals(this.audioTokens, another.audioTokens);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.textTokens);
        h += (h << 5) + Objects.hashCode(this.audioTokens);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "AudioTokenDetailsUsage { textTokens = " + this.textTokens + ", audioTokens = " + this.audioTokens + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private Integer textTokens;
        private Integer audioTokens;

        public Builder textTokens(Integer textTokens) {
            this.textTokens = textTokens;
            return this;
        }

        public Builder audioTokens(Integer audioTokens) {
            this.audioTokens = audioTokens;
            return this;
        }

        public AudioTokenDetailsUsage build() {
            return new AudioTokenDetailsUsage(this);
        }
    }
}

