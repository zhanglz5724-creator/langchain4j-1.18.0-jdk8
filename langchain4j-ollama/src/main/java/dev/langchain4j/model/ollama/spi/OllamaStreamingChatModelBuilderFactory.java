/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.ollama.spi;

import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import java.util.function.Supplier;

public interface OllamaStreamingChatModelBuilderFactory
extends Supplier<OllamaStreamingChatModel.OllamaStreamingChatModelBuilder> {
}

