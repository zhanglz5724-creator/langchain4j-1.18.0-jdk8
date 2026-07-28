/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.protocol;

import dev.langchain4j.mcp.protocol.McpClientParams;
import java.util.Objects;

public class McpUnsubscribeResourceParams
extends McpClientParams {
    private String uri;

    public McpUnsubscribeResourceParams(String uri) {
        this.uri = Objects.requireNonNull(uri);
    }

    public String getUri() {
        return this.uri;
    }
}

