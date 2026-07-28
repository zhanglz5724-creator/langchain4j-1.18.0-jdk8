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
 *  dev.langchain4j.internal.Utils
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
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.openai.internal.audio.transcription.AudioTokenDetailsUsage;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AudioTokenUsage {
    @JsonProperty
    private final String type;
    @JsonProperty
    private final Integer totalTokens;
    @JsonProperty
    private final Integer inputTokens;
    @JsonProperty
    private final AudioTokenDetailsUsage inputTokenDetails;
    @JsonProperty
    private final Integer outputTokens;

    public AudioTokenUsage(Builder builder) {
        this.type = builder.type;
        this.totalTokens = builder.totalTokens;
        this.inputTokens = builder.inputTokens;
        this.inputTokenDetails = builder.inputTokenDetails;
        this.outputTokens = builder.outputTokens;
    }

    public String type() {
        return this.type;
    }

    public Integer totalTokens() {
        return this.totalTokens;
    }

    public Integer inputTokens() {
        return this.inputTokens;
    }

    public AudioTokenDetailsUsage inputTokenDetails() {
        return this.inputTokenDetails;
    }

    public Integer outputTokens() {
        return this.outputTokens;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof AudioTokenUsage && this.equalTo((AudioTokenUsage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(AudioTokenUsage another) {
        return Objects.equals(this.type, another.type) && Objects.equals(this.totalTokens, another.totalTokens) && Objects.equals(this.inputTokens, another.inputTokens) && Objects.equals(this.inputTokenDetails, another.inputTokenDetails) && Objects.equals(this.outputTokens, another.outputTokens);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.type);
        h += (h << 5) + Objects.hashCode(this.totalTokens);
        h += (h << 5) + Objects.hashCode(this.inputTokens);
        h += (h << 5) + Objects.hashCode(this.inputTokenDetails);
        h += (h << 5) + Objects.hashCode(this.outputTokens);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "AudioTokenUsage{ type = " + Utils.quoted((Object)this.type) + ", totalTokens = " + this.totalTokens + " inputTokens = " + this.inputTokens + ", inputTokenDetails = " + this.inputTokenDetails + " outputTokens = " + this.outputTokens + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private String type;
        private Integer totalTokens;
        private Integer inputTokens;
        private AudioTokenDetailsUsage inputTokenDetails;
        private Integer outputTokens;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public Builder inputTokens(Integer inputTokens) {
            this.inputTokens = inputTokens;
            return this;
        }

        public Builder inputTokenDetails(AudioTokenDetailsUsage inputTokenDetails) {
            this.inputTokenDetails = inputTokenDetails;
            return this;
        }

        public Builder outputTokens(Integer outputTokens) {
            this.outputTokens = outputTokens;
            return this;
        }

        public AudioTokenUsage build() {
            return new AudioTokenUsage(this);
        }
    }
}

