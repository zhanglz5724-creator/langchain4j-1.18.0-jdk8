/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAlias
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class McpMetadata {
    private Long count;
    @JsonAlias(value={"next_cursor"})
    private String nextCursor;

    public Long getCount() {
        return this.count;
    }

    public String getNextCursor() {
        return this.nextCursor;
    }

    public String toString() {
        return "McpMetadata{count=" + this.count + ", nextCursor='" + this.nextCursor + '\'' + '}';
    }
}

