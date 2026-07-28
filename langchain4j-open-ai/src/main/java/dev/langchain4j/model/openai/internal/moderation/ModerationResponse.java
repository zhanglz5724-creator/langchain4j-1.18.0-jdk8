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
package dev.langchain4j.model.openai.internal.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.ChatCompletionRequest;
import dev.langchain4j.model.openai.internal.moderation.ModerationResult;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = ModerationResponse.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class ModerationResponse {
    @JsonProperty
    private final String id;
    @JsonProperty
    private final String model;
    @JsonProperty
    private final List<ModerationResult> results;

    public ModerationResponse(Builder builder) {
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

    public List<ModerationResult> results() {
        return this.results;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ModerationResponse && this.equalTo((ModerationResponse)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ModerationResponse another) {
        return Objects.equals(this.id, another.id) && Objects.equals(this.model, another.model) && Objects.equals(this.results, another.results);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.id);
        h += (h << 5) + Objects.hashCode(this.model);
        h += (h << 5) + Objects.hashCode(this.results);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ModerationResponse{id=" + this.id + ", model=" + this.model + ", results=" + this.results + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        public String id;
        public String model;
        public List<ModerationResult> results;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder results(List<ModerationResult> results) {
            if (results != null) {
                this.results = Collections.unmodifiableList(results);
            }
            return this;
        }

        public ModerationResponse build() {
            return new ModerationResponse(this);
        }
    }
}

