/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.service.tool.ToolErrorContext;
import dev.langchain4j.service.tool.ToolErrorHandlerResult;

@FunctionalInterface
public interface ToolExecutionErrorHandler {
    public ToolErrorHandlerResult handle(Throwable var1, ToolErrorContext var2);
}

