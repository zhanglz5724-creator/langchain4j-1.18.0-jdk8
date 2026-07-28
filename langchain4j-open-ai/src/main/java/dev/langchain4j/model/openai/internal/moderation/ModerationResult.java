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
import dev.langchain4j.model.openai.internal.moderation.Categories;
import dev.langchain4j.model.openai.internal.moderation.CategoryScores;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class ModerationResult {
    @JsonProperty
    private final Categories categories;
    @JsonProperty
    private final CategoryScores categoryScores;
    @JsonProperty
    private final Boolean flagged;

    public ModerationResult(Builder builder) {
        this.categories = builder.categories;
        this.categoryScores = builder.categoryScores;
        this.flagged = builder.flagged;
    }

    public Categories categories() {
        return this.categories;
    }

    public CategoryScores categoryScores() {
        return this.categoryScores;
    }

    public Boolean isFlagged() {
        return this.flagged;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ModerationResult && this.equalTo((ModerationResult)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ModerationResult another) {
        return Objects.equals(this.categories, another.categories) && Objects.equals(this.categoryScores, another.categoryScores) && Objects.equals(this.flagged, another.flagged);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.categories);
        h += (h << 5) + Objects.hashCode(this.categoryScores);
        h += (h << 5) + Objects.hashCode(this.flagged);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ModerationResult{categories=" + this.categories + ", categoryScores=" + this.categoryScores + ", flagged=" + this.flagged + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private Categories categories;
        private CategoryScores categoryScores;
        private Boolean flagged;

        public Builder categories(Categories categories) {
            this.categories = categories;
            return this;
        }

        public Builder categoryScores(CategoryScores categoryScores) {
            this.categoryScores = categoryScores;
            return this;
        }

        public Builder flagged(Boolean flagged) {
            this.flagged = flagged;
            return this;
        }

        public ModerationResult build() {
            return new ModerationResult(this);
        }
    }
}

