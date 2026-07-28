/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.spi;

import dev.langchain4j.model.vertexai.VertexAiLanguageModel;
import java.util.function.Supplier;

public interface VertexAiLanguageModelBuilderFactory
extends Supplier<VertexAiLanguageModel.Builder> {
}

