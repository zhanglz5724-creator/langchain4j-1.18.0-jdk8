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
package dev.langchain4j.model.openai.internal.completion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.completion.CompletionChoice;
import dev.langchain4j.model.openai.internal.shared.Usage;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class CompletionResponse {
    @JsonProperty
    private final String id;
    @JsonProperty
    private final Integer created;
    @JsonProperty
    private final String model;
    @JsonProperty
    private final List<CompletionChoice> choices;
    @JsonProperty
    private final Usage usage;

    public CompletionResponse(Builder builder) {
        this.id = builder.id;
        this.created = builder.created;
        this.model = builder.model;
        this.choices = builder.choices;
        this.usage = builder.usage;
    }

    public String id() {
        return this.id;
    }

    public Integer created() {
        return this.created;
    }

    public String model() {
        return this.model;
    }

    public List<CompletionChoice> choices() {
        return this.choices;
    }

    public Usage usage() {
        return this.usage;
    }

    public String text() {
        return this.choices().get(0).text();
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof CompletionResponse && this.equalTo((CompletionResponse)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(CompletionResponse another) {
        return Objects.equals(this.id, another.id) && Objects.equals(this.created, another.created) && Objects.equals(this.model, another.model) && Objects.equals(this.choices, another.choices) && Objects.equals(this.usage, another.usage);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.id);
        h += (h << 5) + Objects.hashCode(this.created);
        h += (h << 5) + Objects.hashCode(this.model);
        h += (h << 5) + Objects.hashCode(this.choices);
        h += (h << 5) + Objects.hashCode(this.usage);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "CompletionResponse{id=" + this.id + ", created=" + this.created + ", model=" + this.model + ", choices=" + this.choices + ", usage=" + this.usage + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String id;
        private Integer created;
        private String model;
        private List<CompletionChoice> choices;
        private Usage usage;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder created(Integer created) {
            this.created = created;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder choices(List<CompletionChoice> choices) {
            if (choices != null) {
                this.choices = Collections.unmodifiableList(choices);
            }
            return this;
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public CompletionResponse build() {
            return new CompletionResponse(this);
        }
    }
}

