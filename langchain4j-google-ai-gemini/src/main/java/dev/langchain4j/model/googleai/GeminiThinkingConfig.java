/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public final class GeminiThinkingConfig {
    private final Boolean includeThoughts;
    private final Integer thinkingBudget;
    private final String thinkingLevel;

    @JsonCreator
    public GeminiThinkingConfig(@JsonProperty(value="includeThoughts") Boolean includeThoughts, @JsonProperty(value="thinkingBudget") Integer thinkingBudget, @JsonProperty(value="thinkingLevel") String thinkingLevel) {
        this.includeThoughts = includeThoughts;
        this.thinkingBudget = thinkingBudget;
        this.thinkingLevel = thinkingLevel;
    }

    public Boolean includeThoughts() {
        return this.includeThoughts;
    }

    public Integer thinkingBudget() {
        return this.thinkingBudget;
    }

    public String thinkingLevel() {
        return this.thinkingLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiThinkingConfig)) {
            return false;
        }
        GeminiThinkingConfig that = (GeminiThinkingConfig)o;
        return Objects.equals(this.includeThoughts, that.includeThoughts) && Objects.equals(this.thinkingBudget, that.thinkingBudget) && Objects.equals(this.thinkingLevel, that.thinkingLevel);
    }

    public int hashCode() {
        return Objects.hash(this.includeThoughts, this.thinkingBudget, this.thinkingLevel);
    }

    public String toString() {
        return "GeminiThinkingConfig[includeThoughts=" + this.includeThoughts + ", thinkingBudget=" + this.thinkingBudget + ", thinkingLevel=" + this.thinkingLevel + "]";
    }

    public static class Builder {
        private Boolean includeThoughts;
        private Integer thinkingBudget;
        private String thinkingLevel;

        public Builder includeThoughts(Boolean includeThoughts) {
            this.includeThoughts = includeThoughts;
            return this;
        }

        public Builder thinkingBudget(Integer thinkingBudget) {
            this.thinkingBudget = thinkingBudget;
            return this;
        }

        public Builder thinkingLevel(String thinkingLevel) {
            this.thinkingLevel = thinkingLevel;
            return this;
        }

        public Builder thinkingLevel(GeminiThinkingLevel thinkingLevel) {
            this.thinkingLevel = thinkingLevel.toString().toLowerCase();
            return this;
        }

        public GeminiThinkingConfig build() {
            return new GeminiThinkingConfig(this.includeThoughts, this.thinkingBudget, this.thinkingLevel);
        }
    }

    public static enum GeminiThinkingLevel {
        MINIMAL,
        LOW,
        MEDIUM,
        HIGH;

    }
}

