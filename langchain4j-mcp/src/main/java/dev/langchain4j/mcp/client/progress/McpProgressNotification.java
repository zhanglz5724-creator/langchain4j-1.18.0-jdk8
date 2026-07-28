/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 */
package dev.langchain4j.mcp.client.progress;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;

public class McpProgressNotification {
    private final String progressToken;
    private final double progress;
    private final Double total;
    private final String message;

    public McpProgressNotification(String progressToken, double progress, Double total, String message) {
        this.progressToken = progressToken;
        this.progress = progress;
        this.total = total;
        this.message = message;
    }

    public static McpProgressNotification fromJson(JsonNode params) {
        String progressToken = params.path("progressToken").asText(null);
        double progress = params.path("progress").asDouble();
        Double total = params.has("total") ? Double.valueOf(params.get("total").asDouble()) : null;
        String message = params.has("message") ? params.get("message").asText() : null;
        return new McpProgressNotification(progressToken, progress, total, message);
    }

    public String progressToken() {
        return this.progressToken;
    }

    public double progress() {
        return this.progress;
    }

    public Double total() {
        return this.total;
    }

    public String message() {
        return this.message;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpProgressNotification that = (McpProgressNotification)obj;
        return Double.compare(this.progress, that.progress) == 0 && Objects.equals(this.progressToken, that.progressToken) && Objects.equals(this.total, that.total) && Objects.equals(this.message, that.message);
    }

    public int hashCode() {
        return Objects.hash(this.progressToken, this.progress, this.total, this.message);
    }

    public String toString() {
        return "McpProgressNotification[progressToken=" + this.progressToken + ", progress=" + this.progress + ", total=" + this.total + ", message=" + this.message + ']';
    }
}

