/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.openai.spi;

import dev.langchain4j.Internal;
import dev.langchain4j.model.openai.OpenAiTextToSpeechModel;
import java.util.function.Supplier;

@Internal
public interface OpenAiTextToSpeechModelBuilderFactory
extends Supplier<OpenAiTextToSpeechModel.Builder> {
}

