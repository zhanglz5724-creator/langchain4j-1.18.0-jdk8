/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ollama.spi;

import dev.langchain4j.model.ollama.OllamaChatModel;
import java.util.function.Supplier;

public interface OllamaChatModelBuilderFactory
extends Supplier<OllamaChatModel.OllamaChatModelBuilder> {
}

