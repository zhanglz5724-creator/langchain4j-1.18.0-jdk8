/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.agent.tool;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecificationJsonCodec;

@Internal
public interface ToolSpecificationJsonCodecFactory {
    public ToolSpecificationJsonCodec create();
}

