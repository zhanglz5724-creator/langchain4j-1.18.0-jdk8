/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai.spi;

import dev.langchain4j.model.mistralai.MistralAiEmbeddingModel;
import java.util.function.Supplier;

public interface MistralAiEmbeddingModelBuilderFactory
extends Supplier<MistralAiEmbeddingModel.MistralAiEmbeddingModelBuilder> {
}

