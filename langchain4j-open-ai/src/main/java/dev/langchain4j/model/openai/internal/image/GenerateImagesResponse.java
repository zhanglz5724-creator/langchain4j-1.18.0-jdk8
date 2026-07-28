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
import dev.langchain4j.model.openai.internal.image.ImageData;
import dev.langchain4j.model.openai.internal.image.ImageUsage;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = GenerateImagesResponse.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateImagesResponse {
    @JsonProperty
    private final Long created;
    @JsonProperty
    private final List<ImageData> data;
    @JsonProperty
    private final String background;
    @JsonProperty
    private final String outputFormat;
    @JsonProperty
    private final String quality;
    @JsonProperty
    private final String size;
    @JsonProperty
    private final ImageUsage usage;

    public GenerateImagesResponse(Builder builder) {
        this.created = builder.created;
        this.data = builder.data;
        this.background = builder.background;
        this.outputFormat = builder.outputFormat;
        this.quality = builder.quality;
        this.size = builder.size;
        this.usage = builder.usage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long created() {
        return this.created;
    }

    public List<ImageData> data() {
        return this.data;
    }

    public String background() {
        return this.background;
    }

    public String outputFormat() {
        return this.outputFormat;
    }

    public String quality() {
        return this.quality;
    }

    public String size() {
        return this.size;
    }

    public ImageUsage usage() {
        return this.usage;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "GenerateImagesResponse{created=" + this.created + ", data=" + this.data + ", background=" + this.background + ", outputFormat=" + this.outputFormat + ", quality=" + this.quality + ", size=" + this.size + ", usage=" + this.usage + '}';
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (another == null || this.getClass() != another.getClass()) {
            return false;
        }
        GenerateImagesResponse that = (GenerateImagesResponse)another;
        return Objects.equals(this.created, that.created) && Objects.equals(this.data, that.data) && Objects.equals(this.background, that.background) && Objects.equals(this.outputFormat, that.outputFormat) && Objects.equals(this.quality, that.quality) && Objects.equals(this.size, that.size) && Objects.equals(this.usage, that.usage);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.created, this.data, this.background, this.outputFormat, this.quality, this.size, this.usage);
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private Long created;
        private List<ImageData> data;
        private String background;
        private String outputFormat;
        private String quality;
        private String size;
        private ImageUsage usage;

        public Builder created(Long created) {
            this.created = created;
            return this;
        }

        public Builder data(List<ImageData> data) {
            this.data = data;
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

        public Builder quality(String quality) {
            this.quality = quality;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder usage(ImageUsage usage) {
            this.usage = usage;
            return this;
        }

        public GenerateImagesResponse build() {
            return new GenerateImagesResponse(this);
        }
    }
}

