/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import java.util.function.Supplier;

public interface AzureOpenAiEmbeddingModelBuilderFactory
extends Supplier<AzureOpenAiEmbeddingModel.Builder> {
}

