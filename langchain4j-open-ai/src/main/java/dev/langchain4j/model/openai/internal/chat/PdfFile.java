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
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PdfFile {
    @JsonProperty(value="file_data")
    private final String fileData;
    @JsonProperty(value="filename")
    private final String filename;

    public PdfFile(Builder builder) {
        this.fileData = builder.fileData;
        this.filename = builder.filename;
    }

    public String getFileData() {
        return this.fileData;
    }

    public String getFilename() {
        return this.filename;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof PdfFile && this.equalTo((PdfFile)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(PdfFile another) {
        return Objects.equals(this.fileData, another.fileData) && Objects.equals(this.filename, another.filename);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.fileData);
        h += (h << 5) + Objects.hashCode(this.filename);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "PdfFile{fileData=" + (this.fileData != null ? "[PDF DATA]" : "null") + ", filename=" + this.filename + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String fileData;
        private String filename;

        public Builder fileData(String fileData) {
            this.fileData = fileData;
            return this;
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public PdfFile build() {
            return new PdfFile(this);
        }
    }
}

