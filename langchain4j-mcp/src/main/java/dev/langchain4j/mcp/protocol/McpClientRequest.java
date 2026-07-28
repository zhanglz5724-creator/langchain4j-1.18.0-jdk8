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
import dev.langchain4j.mcp.protocol.McpClientMessage;
import dev.langchain4j.mcp.protocol.McpClientMethod;
import dev.langchain4j.mcp.protocol.McpClientParams;

@Internal
public class McpClientRequest
extends McpClientMessage {
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private McpClientParams params;

    public McpClientRequest(Long id, McpClientMethod method) {
        super(id, method);
    }

    public McpClientParams getParams() {
        return this.params;
    }

    public void setParams(McpClientParams params) {
        this.params = params;
    }
}

