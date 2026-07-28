/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ollama.spi;

import dev.langchain4j.model.ollama.OllamaStreamingLanguageModel;
import java.util.function.Supplier;

public interface OllamaStreamingLanguageModelBuilderFactory
extends Supplier<OllamaStreamingLanguageModel.OllamaStreamingLanguageModelBuilder> {
}

