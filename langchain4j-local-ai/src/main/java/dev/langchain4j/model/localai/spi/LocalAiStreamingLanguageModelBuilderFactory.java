/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.localai.spi;

import dev.langchain4j.model.localai.LocalAiStreamingLanguageModel;
import java.util.function.Supplier;

public interface LocalAiStreamingLanguageModelBuilderFactory
extends Supplier<LocalAiStreamingLanguageModel.LocalAiStreamingLanguageModelBuilder> {
}

