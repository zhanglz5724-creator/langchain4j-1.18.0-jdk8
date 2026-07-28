/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal.spi;

import dev.langchain4j.model.openai.internal.OpenAiClient;
import java.util.function.Supplier;

public interface OpenAiClientBuilderFactory
extends Supplier<OpenAiClient.Builder> {
}

