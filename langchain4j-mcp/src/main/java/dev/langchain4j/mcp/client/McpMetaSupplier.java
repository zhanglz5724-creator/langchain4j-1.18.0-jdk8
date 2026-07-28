/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.mcp.client;

import dev.langchain4j.mcp.client.McpCallContext;
import java.util.Map;
import java.util.function.Function;

@FunctionalInterface
public interface McpMetaSupplier
extends Function<McpCallContext, Map<String, Object>> {
}

