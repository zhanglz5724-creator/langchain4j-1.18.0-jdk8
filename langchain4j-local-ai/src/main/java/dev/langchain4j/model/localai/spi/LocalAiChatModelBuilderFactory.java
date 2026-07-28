/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.localai.spi;

import dev.langchain4j.model.localai.LocalAiChatModel;
import java.util.function.Supplier;

public interface LocalAiChatModelBuilderFactory
extends Supplier<LocalAiChatModel.LocalAiChatModelBuilder> {
}

