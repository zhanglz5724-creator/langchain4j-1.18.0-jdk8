/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.vertexai.spi;

import dev.langchain4j.model.vertexai.VertexAiImageModel;
import java.util.function.Supplier;

public interface VertexAiImageModelBuilderFactory
extends Supplier<VertexAiImageModel.Builder> {
}

