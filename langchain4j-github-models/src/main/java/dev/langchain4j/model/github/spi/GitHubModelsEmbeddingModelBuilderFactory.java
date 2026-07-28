/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.github.spi;

import dev.langchain4j.model.github.GitHubModelsEmbeddingModel;
import java.util.function.Supplier;

@Deprecated
public interface GitHubModelsEmbeddingModelBuilderFactory
extends Supplier<GitHubModelsEmbeddingModel.Builder> {
}

