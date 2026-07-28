/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiStreamingChatModel;
import java.util.function.Supplier;

public interface AzureOpenAiStreamingChatModelBuilderFactory
extends Supplier<AzureOpenAiStreamingChatModel.Builder> {
}

