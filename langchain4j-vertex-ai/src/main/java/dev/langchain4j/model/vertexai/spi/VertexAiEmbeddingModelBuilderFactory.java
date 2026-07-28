/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.spi;

import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import java.util.function.Supplier;

public interface VertexAiEmbeddingModelBuilderFactory
extends Supplier<VertexAiEmbeddingModel.Builder> {
}

