/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import java.util.function.Supplier;

public interface AzureOpenAiChatModelBuilderFactory
extends Supplier<AzureOpenAiChatModel.Builder> {
}

