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
package dev.langchain4j.model.openai.internal.embedding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.embedding.Embedding;
import dev.langchain4j.model.openai.internal.shared.Usage;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = EmbeddingResponse.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class EmbeddingResponse {
    @JsonProperty
    private final String model;
    @JsonProperty
    private final List<Embedding> data;
    @JsonProperty
    private final Usage usage;

    public EmbeddingResponse(Builder builder) {
        this.model = builder.model;
        this.data = builder.data;
        this.usage = builder.usage;
    }

    public String model() {
        return this.model;
    }

    public List<Embedding> data() {
        return this.data;
    }

    public Usage usage() {
        return this.usage;
    }

    public List<Float> embedding() {
        return this.data.get(0).embedding();
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof EmbeddingResponse && this.equalTo((EmbeddingResponse)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(EmbeddingResponse another) {
        return Objects.equals(this.model, another.model) && Objects.equals(this.data, another.data) && Objects.equals(this.usage, another.usage);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.model);
        h += (h << 5) + Objects.hashCode(this.data);
        h += (h << 5) + Objects.hashCode(this.usage);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "EmbeddingResponse{model=" + this.model + ", data=" + this.data + ", usage=" + this.usage + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String model;
        private List<Embedding> data;
        private Usage usage;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder data(List<Embedding> data) {
            if (data != null) {
                this.data = Collections.unmodifiableList(data);
            }
            return this;
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public EmbeddingResponse build() {
            return new EmbeddingResponse(this);
        }
    }
}

