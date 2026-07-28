/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiAudioTranscriptionModel;
import java.util.function.Supplier;

public interface AzureOpenAiAudioTranscriptionModelBuilderFactory
extends Supplier<AzureOpenAiAudioTranscriptionModel.Builder> {
}

