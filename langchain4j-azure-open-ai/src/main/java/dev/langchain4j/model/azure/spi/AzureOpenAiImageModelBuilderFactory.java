/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure.spi;

import dev.langchain4j.model.azure.AzureOpenAiImageModel;
import java.util.function.Supplier;

public interface AzureOpenAiImageModelBuilderFactory
extends Supplier<AzureOpenAiImageModel.Builder> {
}

