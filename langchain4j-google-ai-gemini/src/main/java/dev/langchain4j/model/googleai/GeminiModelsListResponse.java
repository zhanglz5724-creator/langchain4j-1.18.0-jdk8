package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) class GeminiModelsListResponse {
    private final Object @JsonProperty("models";

    public GeminiModelsListResponse(Object @JsonProperty("models") {
        this.@JsonProperty("models" = @JsonProperty("models";
    }

    public Object get@JsonProperty("models"() {
        return @JsonProperty("models";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeminiModelsListResponse that = (GeminiModelsListResponse) o;
        return java.util.Objects.equals(this.@JsonProperty("models", that.@JsonProperty("models");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("models");
    }

    @Override
    public String toString() {
        return "GeminiModelsListResponse{"@JsonProperty("models"=" + @JsonProperty("models" + "}"";
    }

}
