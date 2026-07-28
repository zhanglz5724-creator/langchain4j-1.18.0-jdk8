/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpJsonRpcMessage;
import java.util.List;
import java.util.Map;

@Internal
public class McpListToolsResult
extends McpJsonRpcMessage {
    private final Result result;

    public McpListToolsResult(Long id, Result result) {
        super(id);
        this.result = result;
    }

    public Result getResult() {
        return this.result;
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    public static class Result {
        private final List<Map<String, Object>> tools;
        private final String nextCursor;

        public Result(List<Map<String, Object>> tools, String nextCursor) {
            this.tools = tools;
            this.nextCursor = nextCursor;
        }

        public List<Map<String, Object>> getTools() {
            return this.tools;
        }

        public String getNextCursor() {
            return this.nextCursor;
        }
    }
}

