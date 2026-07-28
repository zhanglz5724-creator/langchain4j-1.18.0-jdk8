/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.registryclient.model;

import java.time.LocalDateTime;

public class McpServerListRequest {
    private final String cursor;
    private final Long limit;
    private final String search;
    private final LocalDateTime updatedSince;
    private final String version;

    private McpServerListRequest(String cursor, Long limit, String search, LocalDateTime updatedSince, String version) {
        this.cursor = cursor;
        this.limit = limit;
        this.search = search;
        this.updatedSince = updatedSince;
        this.version = version;
    }

    public String getCursor() {
        return this.cursor;
    }

    public Long getLimit() {
        return this.limit;
    }

    public String getSearch() {
        return this.search;
    }

    public LocalDateTime getUpdatedSince() {
        return this.updatedSince;
    }

    public String getVersion() {
        return this.version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toString() {
        return "McpServerListRequest{cursor='" + this.cursor + '\'' + ", limit=" + this.limit + ", search='" + this.search + '\'' + ", updatedSince=" + this.updatedSince + ", version='" + this.version + '\'' + '}';
    }

    public static class Builder {
        private String cursor;
        private Long limit;
        private String search;
        private LocalDateTime updatedSince;
        private String version;

        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        public Builder limit(Long limit) {
            this.limit = limit;
            return this;
        }

        public Builder search(String search) {
            this.search = search;
            return this;
        }

        public Builder updatedSince(LocalDateTime updatedSince) {
            this.updatedSince = updatedSince;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public McpServerListRequest build() {
            return new McpServerListRequest(this.cursor, this.limit, this.search, this.updatedSince, this.version);
        }
    }
}

