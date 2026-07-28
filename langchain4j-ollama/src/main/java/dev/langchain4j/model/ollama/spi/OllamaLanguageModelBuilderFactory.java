/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ollama.spi;

import dev.langchain4j.model.ollama.OllamaLanguageModel;
import java.util.function.Supplier;

public interface OllamaLanguageModelBuilderFactory
extends Supplier<OllamaLanguageModel.OllamaLanguageModelBuilder> {
}

