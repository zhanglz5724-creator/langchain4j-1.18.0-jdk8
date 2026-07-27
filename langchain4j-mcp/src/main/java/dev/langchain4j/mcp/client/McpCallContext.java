package dev.langchain4j.mcp.client;

import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.mcp.protocol.McpClientMessage;
import org.jspecify.annotations.Nullable;

/**
 * Context information for any invocation made towards an MCP server.
 *
 * It contains the AI service invocation context when this is during
 * an AI service invocation (in other cases, the invocation context is null).
 */
public class McpCallContext {
    private final @Nullable InvocationContext invocationContext;
    private final McpClientMessage message;

    public McpCallContext(@Nullable InvocationContext invocationContext, McpClientMessage message) {
        this.invocationContext = invocationContext;
        this.message = message;
    }

    public @Nullable InvocationContext getInvocationContext() {
        return invocationContext;
    }

    public McpClientMessage getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McpCallContext that = (McpCallContext) o;
        return java.util.Objects.equals(this.invocationContext, that.invocationContext) && java.util.Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(invocationContext, message);
    }

    @Override
    public String toString() {
        return "McpCallContext{"invocationContext=" + invocationContext + , "message=" + message + "}"";
    }

}
