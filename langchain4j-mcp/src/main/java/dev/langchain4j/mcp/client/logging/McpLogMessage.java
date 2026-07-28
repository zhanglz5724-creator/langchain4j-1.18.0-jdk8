/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.JsonNode
 */
package dev.langchain4j.mcp.client.logging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.mcp.client.logging.McpLogLevel;
import java.util.Objects;

public class McpLogMessage {
    private final McpLogLevel level;
    private final String logger;
    private final JsonNode data;

    @JsonCreator
    public McpLogMessage(@JsonProperty(value="level") McpLogLevel level, @JsonProperty(value="logger") String logger, @JsonProperty(value="data") JsonNode data) {
        this.level = level;
        this.logger = logger;
        this.data = data;
    }

    public static McpLogMessage fromJson(JsonNode json) {
        McpLogLevel level = McpLogLevel.from(json.get("level").asText());
        JsonNode loggerNode = json.get("logger");
        String logger = loggerNode != null ? loggerNode.asText() : null;
        JsonNode data = json.get("data");
        return new McpLogMessage(level, logger, data);
    }

    public McpLogLevel level() {
        return this.level;
    }

    public String logger() {
        return this.logger;
    }

    public JsonNode data() {
        return this.data;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        McpLogMessage that = (McpLogMessage)obj;
        return Objects.equals((Object)this.level, (Object)that.level) && Objects.equals(this.logger, that.logger) && Objects.equals(this.data, that.data);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.level, this.logger, this.data});
    }

    public String toString() {
        return "McpLogMessage[level=" + (Object)((Object)this.level) + ", logger=" + this.logger + ", data=" + this.data + ']';
    }
}

