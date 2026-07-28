/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.oracle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Embedding {
    private int embed_id;
    private String embed_data;
    private String embed_vector;

    @JsonProperty(value="embed_id")
    public void setId(int id) {
        this.embed_id = id;
    }

    @JsonProperty(value="embed_data")
    public void setData(String data) {
        this.embed_data = data;
    }

    @JsonProperty(value="embed_vector")
    public void setVector(String vector) {
        this.embed_vector = vector;
    }

    @JsonProperty(value="embed_id")
    public int getId() {
        return this.embed_id;
    }

    @JsonProperty(value="embed_data")
    public String getData() {
        return this.embed_data;
    }

    @JsonProperty(value="embed_vector")
    public String getVector() {
        return this.embed_vector;
    }
}

