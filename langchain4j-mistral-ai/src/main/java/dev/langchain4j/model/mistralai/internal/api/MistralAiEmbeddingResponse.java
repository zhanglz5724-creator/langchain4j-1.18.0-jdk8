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
import dev.langchain4j.model.mistralai.internal.api.MistralAiEmbedding;
import dev.langchain4j.model.mistralai.internal.api.MistralAiUsage;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiEmbeddingResponseBuilder.class)
public class MistralAiEmbeddingResponse {
    private String id;
    private String object;
    private String model;
    private List<MistralAiEmbedding> data;
    private MistralAiUsage usage;

    private MistralAiEmbeddingResponse(MistralAiEmbeddingResponseBuilder builder) {
        this.id = builder.id;
        this.object = builder.object;
        this.model = builder.model;
        this.data = builder.data;
        this.usage = builder.usage;
    }

    public String getId() {
        return this.id;
    }

    public String getObject() {
        return this.object;
    }

    public String getModel() {
        return this.model;
    }

    public List<MistralAiEmbedding> getData() {
        return this.data;
    }

    public MistralAiUsage getUsage() {
        return this.usage;
    }

    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.object);
        hash = 53 * hash + Objects.hashCode(this.model);
        hash = 53 * hash + Objects.hashCode(this.data);
        hash = 53 * hash + Objects.hashCode(this.usage);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiEmbeddingResponse other = (MistralAiEmbeddingResponse)obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.object, other.object) && Objects.equals(this.model, other.model) && Objects.equals(this.data, other.data) && Objects.equals(this.usage, other.usage);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiEmbeddingResponse [", "]").add("id=" + this.getId()).add("object=" + this.getObject()).add("model=" + this.getModel()).add("data=" + this.getData()).add("usage=" + this.getUsage()).toString();
    }

    public static MistralAiEmbeddingResponseBuilder builder() {
        return new MistralAiEmbeddingResponseBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiEmbeddingResponseBuilder {
        private String id;
        private String object;
        private String model;
        private List<MistralAiEmbedding> data;
        private MistralAiUsage usage;

        private MistralAiEmbeddingResponseBuilder() {
        }

        public MistralAiEmbeddingResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MistralAiEmbeddingResponseBuilder object(String object) {
            this.object = object;
            return this;
        }

        public MistralAiEmbeddingResponseBuilder model(String model) {
            this.model = model;
            return this;
        }

        public MistralAiEmbeddingResponseBuilder data(List<MistralAiEmbedding> data) {
            this.data = data;
            return this;
        }

        public MistralAiEmbeddingResponseBuilder usage(MistralAiUsage usage) {
            this.usage = usage;
            return this;
        }

        public MistralAiEmbeddingResponse build() {
            return new MistralAiEmbeddingResponse(this);
        }
    }
}

