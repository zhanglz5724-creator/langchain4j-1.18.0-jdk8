/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.spi;

import dev.langchain4j.model.workersai.WorkersAiEmbeddingModel;
import java.util.function.Supplier;

public interface WorkersAiEmbeddingModelBuilderFactory
extends Supplier<WorkersAiEmbeddingModel.Builder> {
}

