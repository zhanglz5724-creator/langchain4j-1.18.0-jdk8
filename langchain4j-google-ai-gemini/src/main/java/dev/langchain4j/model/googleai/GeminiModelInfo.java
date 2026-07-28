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
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
final class GeminiModelInfo {
    private final String name;
    private final String baseModelId;
    private final String version;
    private final String displayName;
    private final String description;
    private final Integer inputTokenLimit;
    private final Integer outputTokenLimit;
    private final List<String> supportedGenerationMethods;
    private final Double temperature;
    private final Double maxTemperature;
    private final Double topP;
    private final Integer topK;

    @JsonCreator
    GeminiModelInfo(@JsonProperty(value="name") String name, @JsonProperty(value="baseModelId") String baseModelId, @JsonProperty(value="version") String version, @JsonProperty(value="displayName") String displayName, @JsonProperty(value="description") String description, @JsonProperty(value="inputTokenLimit") Integer inputTokenLimit, @JsonProperty(value="outputTokenLimit") Integer outputTokenLimit, @JsonProperty(value="supportedGenerationMethods") List<String> supportedGenerationMethods, @JsonProperty(value="temperature") Double temperature, @JsonProperty(value="maxTemperature") Double maxTemperature, @JsonProperty(value="topP") Double topP, @JsonProperty(value="topK") Integer topK) {
        this.name = name;
        this.baseModelId = baseModelId;
        this.version = version;
        this.displayName = displayName;
        this.description = description;
        this.inputTokenLimit = inputTokenLimit;
        this.outputTokenLimit = outputTokenLimit;
        this.supportedGenerationMethods = supportedGenerationMethods;
        this.temperature = temperature;
        this.maxTemperature = maxTemperature;
        this.topP = topP;
        this.topK = topK;
    }

    String name() {
        return this.name;
    }

    String baseModelId() {
        return this.baseModelId;
    }

    String version() {
        return this.version;
    }

    String displayName() {
        return this.displayName;
    }

    String description() {
        return this.description;
    }

    Integer inputTokenLimit() {
        return this.inputTokenLimit;
    }

    Integer outputTokenLimit() {
        return this.outputTokenLimit;
    }

    List<String> supportedGenerationMethods() {
        return this.supportedGenerationMethods;
    }

    Double temperature() {
        return this.temperature;
    }

    Double maxTemperature() {
        return this.maxTemperature;
    }

    Double topP() {
        return this.topP;
    }

    Integer topK() {
        return this.topK;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeminiModelInfo)) {
            return false;
        }
        GeminiModelInfo that = (GeminiModelInfo)o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.baseModelId, that.baseModelId) && Objects.equals(this.version, that.version) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.description, that.description) && Objects.equals(this.inputTokenLimit, that.inputTokenLimit) && Objects.equals(this.outputTokenLimit, that.outputTokenLimit) && Objects.equals(this.supportedGenerationMethods, that.supportedGenerationMethods) && Objects.equals(this.temperature, that.temperature) && Objects.equals(this.maxTemperature, that.maxTemperature) && Objects.equals(this.topP, that.topP) && Objects.equals(this.topK, that.topK);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.baseModelId, this.version, this.displayName, this.description, this.inputTokenLimit, this.outputTokenLimit, this.supportedGenerationMethods, this.temperature, this.maxTemperature, this.topP, this.topK);
    }

    public String toString() {
        return "GeminiModelInfo[name=" + this.name + ", baseModelId=" + this.baseModelId + ", version=" + this.version + ", displayName=" + this.displayName + ", description=" + this.description + ", inputTokenLimit=" + this.inputTokenLimit + ", outputTokenLimit=" + this.outputTokenLimit + ", supportedGenerationMethods=" + this.supportedGenerationMethods + ", temperature=" + this.temperature + ", maxTemperature=" + this.maxTemperature + ", topP=" + this.topP + ", topK=" + this.topK + "]";
    }
}

