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
import dev.langchain4j.mcp.protocol.McpClientResponse;
import java.util.HashMap;
import java.util.Map;

@Internal
public class McpPingResponse
extends McpClientResponse {
    @JsonInclude(value=JsonInclude.Include.ALWAYS)
    private final Map<String, Object> result = new HashMap<String, Object>();

    public McpPingResponse(Long id) {
        super(id);
    }

    public Map<String, Object> getResult() {
        return this.result;
    }
}

