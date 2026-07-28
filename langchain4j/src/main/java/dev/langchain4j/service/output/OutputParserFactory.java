/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.OutputParser;

@Internal
interface OutputParserFactory {
    public OutputParser<?> get(Class<?> var1, Class<?> var2);
}

