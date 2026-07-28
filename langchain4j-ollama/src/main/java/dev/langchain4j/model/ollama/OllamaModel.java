/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.ollama.OllamaDateDeserializer;
import dev.langchain4j.model.ollama.OllamaModelDetails;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OllamaModel {
    private String name;
    private String model;
    @JsonDeserialize(using=OllamaDateDeserializer.class)
    private OffsetDateTime modifiedAt;
    private long size;
    private String digest;
    private OllamaModelDetails details;

    OllamaModel() {
    }

    public OllamaModel(String name, long size, String digest, OllamaModelDetails details, OffsetDateTime modifiedAt, String model) {
        this.name = name;
        this.size = size;
        this.digest = digest;
        this.details = details;
        this.modifiedAt = modifiedAt;
        this.model = model;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDigest() {
        return this.digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public OllamaModelDetails getDetails() {
        return this.details;
    }

    public void setDetails(OllamaModelDetails details) {
        this.details = details;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public OffsetDateTime getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(OffsetDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public static class Builder {
        private String name;
        private long size;
        private String digest;
        private OllamaModelDetails details;
        private OffsetDateTime modifiedAt;
        private String model;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder size(long size) {
            this.size = size;
            return this;
        }

        public Builder digest(String digest) {
            this.digest = digest;
            return this;
        }

        public Builder details(OllamaModelDetails details) {
            this.details = details;
            return this;
        }

        public Builder modifiedAt(OffsetDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public OllamaModel build() {
            return new OllamaModel(this.name, this.size, this.digest, this.details, this.modifiedAt, this.model);
        }
    }
}

