/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ollama.spi;

import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import java.util.function.Supplier;

public interface OllamaEmbeddingModelBuilderFactory
extends Supplier<OllamaEmbeddingModel.OllamaEmbeddingModelBuilder> {
}

