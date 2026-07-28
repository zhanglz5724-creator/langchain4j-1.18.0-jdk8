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
import dev.langchain4j.mcp.protocol.McpClientMethod;
import dev.langchain4j.mcp.protocol.McpJsonRpcMessage;

@Internal
public class McpClientMessage
extends McpJsonRpcMessage {
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    public final McpClientMethod method;

    public McpClientMessage(Long id, McpClientMethod method) {
        super(id);
        this.method = method;
    }
}

