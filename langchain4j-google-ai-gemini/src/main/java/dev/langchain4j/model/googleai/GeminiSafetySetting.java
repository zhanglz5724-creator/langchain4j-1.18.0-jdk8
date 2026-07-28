/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.googleai.GeminiHarmBlockThreshold;
import dev.langchain4j.model.googleai.GeminiHarmCategory;

@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GeminiSafetySetting {
    @JsonProperty
    private GeminiHarmCategory category;
    @JsonProperty
    private GeminiHarmBlockThreshold threshold;

    public GeminiSafetySetting(GeminiHarmCategory category, GeminiHarmBlockThreshold threshold) {
        this.category = category;
        this.threshold = threshold;
    }

    public GeminiHarmCategory getCategory() {
        return this.category;
    }

    public GeminiHarmBlockThreshold getThreshold() {
        return this.threshold;
    }

    public void setCategory(GeminiHarmCategory category) {
        this.category = category;
    }

    public void setThreshold(GeminiHarmBlockThreshold threshold) {
        this.threshold = threshold;
    }
}

