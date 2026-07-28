/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiLanguageModel;
import java.util.function.Supplier;

public interface AzureOpenAiLanguageModelBuilderFactory
extends Supplier<AzureOpenAiLanguageModel.Builder> {
}

