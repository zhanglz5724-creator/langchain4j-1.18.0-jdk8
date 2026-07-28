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

public class DirectoryPreference {
    private String dir;

    @JsonIgnore
    public boolean isValid() {
        return this.dir != null;
    }

    @JsonProperty(value="dir")
    public void setDirectory(String dir) {
        this.dir = dir;
    }

    @JsonProperty(value="dir")
    public String getDirectory() {
        return this.dir;
    }
}

