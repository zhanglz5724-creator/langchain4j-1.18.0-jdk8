/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAlias
 */
package dev.langchain4j.mcp.registryclient.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public class McpOfficialMeta {
    @JsonAlias(value={"id"})
    private String serverId;
    @JsonAlias(value={"is_latest"})
    private boolean isLatest;
    @JsonAlias(value={"published_at"})
    private LocalDateTime publishedAt;
    @JsonAlias(value={"updated_at"})
    private LocalDateTime updatedAt;
    private String status;

    @Deprecated
    public String getServerId() {
        return this.serverId;
    }

    public boolean isLatest() {
        return this.isLatest;
    }

    public LocalDateTime getPublishedAt() {
        return this.publishedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getStatus() {
        return this.status;
    }

    public String toString() {
        return "McpOfficialMeta{serverId='" + this.serverId + '\'' + ", isLatest=" + this.isLatest + ", publishedAt=" + this.publishedAt + ", updatedAt=" + this.updatedAt + ", status='" + this.status + '\'' + '}';
    }
}

