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
import dev.langchain4j.mcp.client.McpRoot;
import dev.langchain4j.mcp.protocol.McpClientResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Internal
public class McpRootsListResponse
extends McpClientResponse {
    @JsonInclude(value=JsonInclude.Include.ALWAYS)
    private final Map<String, Object> result = new HashMap<String, Object>();

    public McpRootsListResponse(Long id, List<McpRoot> roots) {
        super(id);
        this.result.put("roots", roots);
    }

    public Map<String, Object> getResult() {
        return this.result;
    }
}

