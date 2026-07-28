/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ollama.spi;

import dev.langchain4j.model.ollama.OllamaImageModel;
import java.util.function.Supplier;

public interface OllamaImageModelBuilderFactory
extends Supplier<OllamaImageModel.OllamaImageModelBuilder> {
}

