/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.service.tool.ToolProviderRequest;
import dev.langchain4j.service.tool.ToolProviderResult;

@FunctionalInterface
public interface ToolProvider {
    public ToolProviderResult provideTools(ToolProviderRequest var1);

    default public boolean isDynamic() {
        return false;
    }
}

