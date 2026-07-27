/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;

@Internal
public class ToolSpecificationUtils {
    private ToolSpecificationUtils() {
    }

    public static boolean isEffectivelyStrict(ToolSpecification toolSpecification, boolean modelLevelStrict) {
        return toolSpecification.strict() != null ? toolSpecification.strict() : modelLevelStrict;
    }
}

