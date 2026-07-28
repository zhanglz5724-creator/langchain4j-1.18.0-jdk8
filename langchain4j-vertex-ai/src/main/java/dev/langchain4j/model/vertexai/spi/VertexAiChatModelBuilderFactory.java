/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.spi;

import dev.langchain4j.model.vertexai.VertexAiChatModel;
import java.util.function.Supplier;

public interface VertexAiChatModelBuilderFactory
extends Supplier<VertexAiChatModel.Builder> {
}

