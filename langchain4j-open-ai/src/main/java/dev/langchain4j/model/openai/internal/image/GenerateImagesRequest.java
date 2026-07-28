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
package dev.langchain4j.model.openai.internal.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import java.util.Objects;

@JsonDeserialize(builder = GenerateImagesRequest.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateImagesRequest {
    @JsonProperty
    private final String model;
    @JsonProperty
    private final String prompt;
    @JsonProperty
    private final int n;
    @JsonProperty
    private final String size;
    @JsonProperty
    private final String quality;
    @JsonProperty
    private final String user;
    @JsonProperty
    private final String background;
    @JsonProperty
    private final String outputFormat;
    @JsonProperty
    private final Integer outputCompression;
    @JsonProperty
    private final String moderation;

    public GenerateImagesRequest(Builder builder) {
        this.model = builder.model;
        this.prompt = builder.prompt;
        this.n = builder.n;
        this.size = builder.size;
        this.quality = builder.quality;
        this.user = builder.user;
        this.background = builder.background;
        this.outputFormat = builder.outputFormat;
        this.outputCompression = builder.outputCompression;
        this.moderation = builder.moderation;
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 2381;
        h += (h << 5) + Objects.hashCode(this.model);
        h += (h << 5) + Objects.hashCode(this.prompt);
        h += (h << 5) + this.n;
        h += (h << 5) + Objects.hashCode(this.size);
        h += (h << 5) + Objects.hashCode(this.quality);
        h += (h << 5) + Objects.hashCode(this.user);
        h += (h << 5) + Objects.hashCode(this.background);
        h += (h << 5) + Objects.hashCode(this.outputFormat);
        h += (h << 5) + Objects.hashCode(this.outputCompression);
        h += (h << 5) + Objects.hashCode(this.moderation);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "GenerateImagesRequest{model=" + this.model + ", prompt=" + this.prompt + ", n=" + this.n + ", size=" + this.size + ", quality=" + this.quality + ", user=" + this.user + ", background=" + this.background + ", outputFormat=" + this.outputFormat + ", outputCompression=" + this.outputCompression + ", moderation=" + this.moderation + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private String model;
        private String prompt;
        private int n = 1;
        private String size;
        private String quality;
        private String user;
        private String background;
        private String outputFormat;
        private Integer outputCompression;
        private String moderation;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder n(int n) {
            this.n = n;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder quality(String quality) {
            this.quality = quality;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder background(String background) {
            this.background = background;
            return this;
        }

        public Builder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public Builder outputCompression(Integer outputCompression) {
            this.outputCompression = outputCompression;
            return this;
        }

        public Builder moderation(String moderation) {
            this.moderation = moderation;
            return this;
        }

        public GenerateImagesRequest build() {
            return new GenerateImagesRequest(this);
        }
    }
}

