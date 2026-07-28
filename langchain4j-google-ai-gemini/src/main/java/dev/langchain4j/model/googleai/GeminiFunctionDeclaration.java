/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.googleai.GeminiSchema;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiFunctionDeclaration {
    private final String name;
    private final String description;
    private final GeminiSchema parameters;

    @JsonCreator
    GeminiFunctionDeclaration(@JsonProperty(value="name") String name, @JsonProperty(value="description") String description, @JsonProperty(value="parameters") GeminiSchema parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    String name() {
        return this.name;
    }

    String description() {
        return this.description;
    }

    GeminiSchema parameters() {
        return this.parameters;
    }

    static Builder builder() {
        return new Builder();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiFunctionDeclaration)) {
            return false;
        }
        GeminiFunctionDeclaration that = (GeminiFunctionDeclaration)o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.parameters, that.parameters);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.description, this.parameters);
    }

    public String toString() {
        return "GeminiFunctionDeclaration[name=" + this.name + ", description=" + this.description + ", parameters=" + this.parameters + "]";
    }

    static class Builder {
        private String name;
        private String description;
        private GeminiSchema parameters;

        private Builder() {
        }

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
            return new GeminiFunctionDeclaration(this.name, this.description, this.parameters);
        }
    }
}

