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

@Internal
public class McpJsonRpcMessage {
    @JsonInclude
    public final String jsonrpc = "2.0";
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private Long id;

    public McpJsonRpcMessage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

