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
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelCard;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiModelResponseBuilder.class)
public class MistralAiModelResponse {
    private String object;
    private List<MistralAiModelCard> data;

    private MistralAiModelResponse(MistralAiModelResponseBuilder builder) {
        this.object = builder.object;
        this.data = builder.data;
    }

    public String getObject() {
        return this.object;
    }

    public List<MistralAiModelCard> getData() {
        return this.data;
    }

    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.object);
        hash = 47 * hash + Objects.hashCode(this.data);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiModelResponse other = (MistralAiModelResponse)obj;
        return Objects.equals(this.object, other.object) && Objects.equals(this.data, other.data);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiModelResponse [", "]").add("object=" + this.getObject()).add("data=" + this.getData()).toString();
    }

    public static MistralAiModelResponseBuilder builder() {
        return new MistralAiModelResponseBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiModelResponseBuilder {
        private String object;
        private List<MistralAiModelCard> data;

        private MistralAiModelResponseBuilder() {
        }

        public MistralAiModelResponseBuilder object(String object) {
            this.object = object;
            return this;
        }

        public MistralAiModelResponseBuilder data(List<MistralAiModelCard> data) {
            this.data = data;
            return this;
        }

        public MistralAiModelResponse build() {
            return new MistralAiModelResponse(this);
        }
    }
}

