/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.data.document.splitter.oracle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Chunk {
    private int chunk_id;
    private int chunk_offset;
    private int chunk_length;
    private String chunk_data;

    @JsonProperty(value="chunk_id")
    public void setId(int id) {
        this.chunk_id = id;
    }

    @JsonProperty(value="chunk_offset")
    public void setOffset(int offset) {
        this.chunk_offset = offset;
    }

    @JsonProperty(value="chunk_length")
    public void setLength(int length) {
        this.chunk_length = length;
    }

    @JsonProperty(value="chunk_data")
    public void setData(String data) {
        this.chunk_data = data;
    }

    @JsonProperty(value="chunk_id")
    public int getId() {
        return this.chunk_id;
    }

    @JsonProperty(value="chunk_offset")
    public int getOffset() {
        return this.chunk_offset;
    }

    @JsonProperty(value="chunk_length")
    public int getLength() {
        return this.chunk_length;
    }

    @JsonProperty(value="chunk_data")
    public String getData() {
        return this.chunk_data;
    }
}

