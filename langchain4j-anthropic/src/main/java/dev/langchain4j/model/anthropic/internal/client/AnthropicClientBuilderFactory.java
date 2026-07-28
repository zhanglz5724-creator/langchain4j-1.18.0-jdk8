/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.anthropic.internal.client;

import dev.langchain4j.Internal;
import dev.langchain4j.model.anthropic.internal.client.AnthropicClient;
import java.util.function.Supplier;

@Internal
public interface AnthropicClientBuilderFactory
extends Supplier<AnthropicClient.Builder> {
}

