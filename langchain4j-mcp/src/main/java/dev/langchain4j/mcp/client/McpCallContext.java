/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.invocation.InvocationContext
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.mcp.client;

import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public class McpCallContext {
    private final InvocationContext invocationContext;
    private final McpClientMessage message;

    public McpCallContext(@Nullable InvocationContext invocationContext, McpClientMessage message) {
        this.invocationContext = invocationContext;
        this.message = message;
    }

    public @Nullable InvocationContext invocationContext() {
        return this.invocationContext;
    }

    public McpClientMessage message() {
        return this.message;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof McpCallContext)) {
            return false;
        }
        McpCallContext other = (McpCallContext)o;
        if (!Objects.equals(this.invocationContext, other.invocationContext)) {
            return false;
        }
        return Objects.equals(this.message, other.message);
    }

    public int hashCode() {
        return Objects.hash(this.invocationContext, this.message);
    }

    public String toString() {
        return "McpCallContext{invocationContext=" + Objects.toString(this.invocationContext) + ", message=" + Objects.toString(this.message) + "}";
    }
}

