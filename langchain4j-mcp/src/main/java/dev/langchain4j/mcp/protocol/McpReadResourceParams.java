/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpClientParams;
import java.util.Objects;

@Internal
public class McpReadResourceParams
extends McpClientParams {
    private String uri;

    public McpReadResourceParams() {
    }

    public McpReadResourceParams(String uri) {
        Objects.requireNonNull(uri);
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

