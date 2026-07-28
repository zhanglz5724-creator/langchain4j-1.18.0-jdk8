/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.openai.spi;

import dev.langchain4j.Internal;
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.util.function.Supplier;

@Internal
public interface OpenAiChatModelBuilderFactory
extends Supplier<OpenAiChatModel.OpenAiChatModelBuilder> {
}

