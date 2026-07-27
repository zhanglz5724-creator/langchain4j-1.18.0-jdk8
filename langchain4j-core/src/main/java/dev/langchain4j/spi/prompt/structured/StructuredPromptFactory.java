/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.prompt.structured;

import dev.langchain4j.Internal;
import dev.langchain4j.model.input.Prompt;

@Internal
public interface StructuredPromptFactory {
    public Prompt toPrompt(Object var1);
}

