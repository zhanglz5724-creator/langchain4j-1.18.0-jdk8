package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the media resolution settings for a content part.
 * This is used for per-part media resolution setting (Gemini 3 only).
 *
 * @see <a href="https://ai.google.dev/gemini-api/docs/media-resolution">Media Resolution Documentation</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_NULL) class GeminiMediaResolution {
    private final Object @JsonProperty("level";

    public GeminiMediaResolution(Object @JsonProperty("level") {
        this.@JsonProperty("level" = @JsonProperty("level";
    }

    public Object get@JsonProperty("level"() {
        return @JsonProperty("level";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeminiMediaResolution that = (GeminiMediaResolution) o;
        return java.util.Objects.equals(this.@JsonProperty("level", that.@JsonProperty("level");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("level");
    }

    @Override
    public String toString() {
        return "GeminiMediaResolution{"@JsonProperty("level"=" + @JsonProperty("level" + "}"";
    }

    static GeminiMediaResolution of(GeminiMediaResolutionLevel level) {
        return level != null ? new GeminiMediaResolution(level) : null;
    }
}
