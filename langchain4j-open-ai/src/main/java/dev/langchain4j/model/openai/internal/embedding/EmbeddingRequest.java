/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAnyGetter
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
package dev.langchain4j.model.openai.internal.embedding;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonDeserialize(builder = EmbeddingRequest.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class EmbeddingRequest {
    @JsonProperty
    private final String model;
    @JsonProperty
    private final List<String> input;
    @JsonProperty
    private final Integer dimensions;
    @JsonProperty
    private final String user;
    @JsonProperty
    private final String encodingFormat;
    private final Map<String, Object> customParameters;

    public EmbeddingRequest(Builder builder) {
        this.model = builder.model;
        this.input = builder.input;
        this.dimensions = builder.dimensions;
        this.user = builder.user;
        this.encodingFormat = builder.encodingFormat;
        this.customParameters = builder.customParameters;
    }

    public String model() {
        return this.model;
    }

    public List<String> input() {
        return this.input;
    }

    public Integer dimensions() {
        return this.dimensions;
    }

    public String user() {
        return this.user;
    }

    public String encodingFormat() {
        return this.encodingFormat;
    }

    @JsonAnyGetter
    public Map<String, Object> customParameters() {
        return this.customParameters;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof EmbeddingRequest && this.equalTo((EmbeddingRequest)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(EmbeddingRequest another) {
        return Objects.equals(this.model, another.model) && Objects.equals(this.input, another.input) && Objects.equals(this.dimensions, another.dimensions) && Objects.equals(this.user, another.user) && Objects.equals(this.encodingFormat, another.encodingFormat) && Objects.equals(this.customParameters, another.customParameters);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.model);
        h += (h << 5) + Objects.hashCode(this.input);
        h += (h << 5) + Objects.hashCode(this.dimensions);
        h += (h << 5) + Objects.hashCode(this.user);
        h += (h << 5) + Objects.hashCode(this.encodingFormat);
        h += (h << 5) + Objects.hashCode(this.customParameters);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "EmbeddingRequest{model=" + this.model + ", input=" + this.input + ", dimensions=" + this.dimensions + ", user=" + this.user + ", encodingFormat=" + this.encodingFormat + ", customParameters=" + this.customParameters + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String model;
        private List<String> input;
        private Integer dimensions;
        private String user;
        private String encodingFormat;
        private Map<String, Object> customParameters;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder input(String ... input) {
            return this.input(Arrays.asList(input));
        }

        public Builder input(List<String> input) {
            if (input != null) {
                this.input = Collections.unmodifiableList(input);
            }
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder encodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        public Builder customParameters(Map<String, Object> customParameters) {
            this.customParameters = customParameters;
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }
    }
}

