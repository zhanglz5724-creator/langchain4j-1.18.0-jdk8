/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModerationResult;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonDeserialize(builder=Builder.class)
public final class MistralAiModerationResponse {
    private final String id;
    private final String model;
    private final List<MistralAiModerationResult> results;

    public MistralAiModerationResponse(Builder builder) {
        this.id = builder.id;
        this.model = builder.model;
        this.results = builder.results;
    }

    public String id() {
        return this.id;
    }

    public String model() {
        return this.model;
    }

    public List<MistralAiModerationResult> results() {
        return this.results;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {
        private String id;
        private String model;
        private List<MistralAiModerationResult> results;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder results(List<MistralAiModerationResult> results) {
            this.results = results;
            return this;
        }

        public MistralAiModerationResponse build() {
            return new MistralAiModerationResponse(this);
        }
    }
}

