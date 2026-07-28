/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.gemini.spi;

import dev.langchain4j.model.vertexai.gemini.VertexAiGeminiChatModel;
import java.util.function.Supplier;

public interface VertexAiGeminiChatModelBuilderFactory
extends Supplier<VertexAiGeminiChatModel.VertexAiGeminiChatModelBuilder> {
}

