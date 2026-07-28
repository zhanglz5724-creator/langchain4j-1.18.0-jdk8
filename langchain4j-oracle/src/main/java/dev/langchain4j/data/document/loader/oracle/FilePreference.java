/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.data.document.loader.oracle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilePreference {
    private String filename;

    @JsonIgnore
    public boolean isValid() {
        return this.filename != null;
    }

    @JsonProperty(value="file")
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @JsonProperty(value="file")
    public String getFilename() {
        return this.filename;
    }
}

