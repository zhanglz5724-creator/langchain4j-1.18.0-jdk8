package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) class GeminiFunctionDeclaration {
    private final Object @JsonProperty("name";

    public GeminiFunctionDeclaration(Object @JsonProperty("name") {
        this.@JsonProperty("name" = @JsonProperty("name";
    }

    public Object get@JsonProperty("name"() {
        return @JsonProperty("name";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeminiFunctionDeclaration that = (GeminiFunctionDeclaration) o;
        return java.util.Objects.equals(this.@JsonProperty("name", that.@JsonProperty("name");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(@JsonProperty("name");
    }

    @Override
    public String toString() {
        return "GeminiFunctionDeclaration{"@JsonProperty("name"=" + @JsonProperty("name" + "}"";
    }


    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String name;
        private String description;
        private GeminiSchema parameters;

        private Builder() {}

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder description(String description) {
            this.description = description;
            return this;
        }

        Builder parameters(GeminiSchema parameters) {
            this.parameters = parameters;
            return this;
        }

        GeminiFunctionDeclaration build() {
            return new GeminiFunctionDeclaration(name, description, parameters);
        }
    }
}
