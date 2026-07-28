/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiEmbeddingRequestBuilder.class)
public class MistralAiEmbeddingRequest {
    private String model;
    private List<String> input;
    private String encodingFormat;

    public MistralAiEmbeddingRequest() {
    }

    public MistralAiEmbeddingRequest(MistralAiEmbeddingRequestBuilder builder) {
        this.model = builder.model;
        this.input = builder.input;
        this.encodingFormat = builder.encodingFormat;
    }

    public String getModel() {
        return this.model;
    }

    public List<String> getInput() {
        return this.input;
    }

    public String getEncodingFormat() {
        return this.encodingFormat;
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.model);
        hash = 29 * hash + Objects.hashCode(this.input);
        hash = 29 * hash + Objects.hashCode(this.encodingFormat);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiEmbeddingRequest other = (MistralAiEmbeddingRequest)obj;
        return Objects.equals(this.model, other.model) && Objects.equals(this.encodingFormat, other.encodingFormat) && Objects.equals(this.input, other.input);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiEmbeddingRequest [", "]").add("model=" + this.getModel()).add("input=" + this.getInput()).add("encodingFormat=" + this.getEncodingFormat()).toString();
    }

    public static MistralAiEmbeddingRequestBuilder builder() {
        return new MistralAiEmbeddingRequestBuilder();
    }

    public MistralAiEmbeddingRequestBuilder toBuilder() {
        return new MistralAiEmbeddingRequestBuilder().model(this.model).input(this.input).encodingFormat(this.encodingFormat);
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiEmbeddingRequestBuilder {
        private String model;
        private List<String> input;
        private String encodingFormat;

        private MistralAiEmbeddingRequestBuilder() {
        }

        public MistralAiEmbeddingRequestBuilder model(String model) {
            this.model = model;
            return this;
        }

        public MistralAiEmbeddingRequestBuilder input(List<String> input) {
            this.input = input;
            return this;
        }

        public MistralAiEmbeddingRequestBuilder encodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        public MistralAiEmbeddingRequest build() {
            return new MistralAiEmbeddingRequest(this);
        }

        public String toString() {
            return "MistralAiEmbeddingRequest.MistralAiEmbeddingRequestBuilder(model=" + this.model + ", input=" + this.input + ", encodingFormat=" + this.encodingFormat + ")";
        }
    }
}

