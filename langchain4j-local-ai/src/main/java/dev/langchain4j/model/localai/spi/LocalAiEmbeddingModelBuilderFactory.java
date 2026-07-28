/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.localai.spi;

import dev.langchain4j.model.localai.LocalAiEmbeddingModel;
import java.util.function.Supplier;

public interface LocalAiEmbeddingModelBuilderFactory
extends Supplier<LocalAiEmbeddingModel.LocalAiEmbeddingModelBuilder> {
}

