/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpClientParams;

@Internal
public class McpCallToolParams
extends McpClientParams {
    private String name;
    @JsonInclude(value=JsonInclude.Include.ALWAYS)
    private ObjectNode arguments;

    public McpCallToolParams() {
    }

    public McpCallToolParams(String name, ObjectNode arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectNode getArguments() {
        return this.arguments;
    }

    public void setArguments(ObjectNode arguments) {
        this.arguments = arguments;
    }
}

