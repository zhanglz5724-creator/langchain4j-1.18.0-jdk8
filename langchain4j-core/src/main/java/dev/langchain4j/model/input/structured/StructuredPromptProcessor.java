/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.input.structured;

import dev.langchain4j.Internal;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.DefaultStructuredPromptFactory;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.prompt.structured.StructuredPromptFactory;
import java.util.Iterator;

@Internal
public class StructuredPromptProcessor {
    private static final StructuredPromptFactory FACTORY = StructuredPromptProcessor.factory();

    private StructuredPromptProcessor() {
    }

    private static StructuredPromptFactory factory() {
        Iterator<StructuredPromptFactory> iterator = ServiceHelper.loadFactories(StructuredPromptFactory.class).iterator();
        if (iterator.hasNext()) {
            StructuredPromptFactory factory = iterator.next();
            return factory;
        }
        return new DefaultStructuredPromptFactory();
    }

    public static Prompt toPrompt(Object structuredPrompt) {
        return FACTORY.toPrompt(structuredPrompt);
    }
}

