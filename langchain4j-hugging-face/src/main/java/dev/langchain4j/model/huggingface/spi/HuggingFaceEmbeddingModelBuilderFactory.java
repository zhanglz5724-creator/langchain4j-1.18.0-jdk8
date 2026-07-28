/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.huggingface.spi;

import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import java.util.function.Supplier;

public interface HuggingFaceEmbeddingModelBuilderFactory
extends Supplier<HuggingFaceEmbeddingModel.HuggingFaceEmbeddingModelBuilder> {
}

