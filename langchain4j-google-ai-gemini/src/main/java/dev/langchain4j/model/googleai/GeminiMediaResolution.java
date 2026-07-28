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
import dev.langchain4j.model.googleai.GeminiMediaResolutionLevel;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
final class GeminiMediaResolution {
    private final GeminiMediaResolutionLevel level;

    @JsonCreator
    GeminiMediaResolution(@JsonProperty(value="level") GeminiMediaResolutionLevel level) {
        this.level = level;
    }

    GeminiMediaResolutionLevel level() {
        return this.level;
    }

    static GeminiMediaResolution of(GeminiMediaResolutionLevel level) {
        return level != null ? new GeminiMediaResolution(level) : null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiMediaResolution)) {
            return false;
        }
        GeminiMediaResolution that = (GeminiMediaResolution)o;
        return this.level == that.level;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.level});
    }

    public String toString() {
        return "GeminiMediaResolution[level=" + (Object)((Object)this.level) + "]";
    }
}

