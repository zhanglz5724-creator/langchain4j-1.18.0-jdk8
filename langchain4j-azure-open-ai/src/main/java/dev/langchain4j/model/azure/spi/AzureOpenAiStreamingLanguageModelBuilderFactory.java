/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiStreamingLanguageModel;
import java.util.function.Supplier;

public interface AzureOpenAiStreamingLanguageModelBuilderFactory
extends Supplier<AzureOpenAiStreamingLanguageModel.Builder> {
}

